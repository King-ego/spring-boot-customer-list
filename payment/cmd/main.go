package main

import (
	"encoding/json"
	"log"
	"time"

	payment "com.ecommerce.payment"
	"github.com/streadway/amqp"
)

type OrderCreatedEvent struct {
	GroupID     string  `json:"groupId"`
	CustomerID  string  `json:"customerId"`
	OrderStatus string  `json:"orderStatus"`
	OrderTotal  float64 `json:"orderTotal"`
}

/* type PaymentProcessedEvent struct {
    GroupID    string `json:"groupId"`
    CustomerID string `json:"customerId"`
    Status     string `json:"status"`
    PaymentId  string `json:"paymentId"`
    PaymentDate string `json:"paymentDate"`
} */

func connectToRabbitMQ() (*amqp.Connection, error) {
	var conn *amqp.Connection
	var err error

	/* amqpUrl := os.Getenv("RABBITMQ_URL")
	if amqpUrl != "" {
		amqpUrl = "amqp://guest:guest@rabbitmq:5672/"
	} */

	for i := 0; i < 30; i++ {
		conn, err = amqp.Dial("amqp://guest:guest@rabbitmq:5672/")
		if err == nil {
			log.Println("Connected to RabbitMQ!")
			return conn, nil
		}

		log.Printf("Failed to connect to RabbitMQ (attempt %d/30): %s", i+1, err)
		time.Sleep(2 * time.Second)
	}

	return nil, err
}

func main() {
	log.Println("Payment service starting...")

	conn, err := connectToRabbitMQ()
	if err != nil {
		log.Fatalf("Failed to connect to RabbitMQ after 30 attempts: %s", err)
	}
	defer func(conn *amqp.Connection) {
		err := conn.Close()
		if err != nil {
			log.Printf("Error closing RabbitMQ connection: %s", err)
		}
	}(conn)

	ch, err := conn.Channel()
	if err != nil {
		log.Fatal("Failed to open a channel: %s", err)
	}
	defer func(ch *amqp.Channel) {
		err := ch.Close()
		if err != nil {
			log.Printf("Error closing channel: %s", err)
		}
	}(ch)

	args := amqp.Table{
		"x-dead-letter-exchange":    "customer-dlx",
		"x-dead-letter-routing-key": "customer.failed",
	}

	queue, err := ch.QueueDeclare(
		"order-queue",
		true,
		false,
		false,
		false,
		args,
	)
	if err != nil {
		log.Fatal("Failed to declare a queue: %s", err)
	}

	msg, err := ch.Consume(
		queue.Name,
		"",
		true,
		false,
		false,
		false,
		nil,
	)

	if err != nil {
		log.Fatal("Failed to register a consumer: %s", err)
	}

	forever := make(chan bool)

	go func() {
		for d := range msg {
			log.Printf("Received a message: %s", d.Body)

			var event OrderCreatedEvent
			err := json.Unmarshal(d.Body, &event)
			if err != nil {
				log.Printf("Error decoding JSON: %s", err)
				continue
			}

			log.Printf("Processing payment for Order ID: %s, Customer: %s, Total: %.2f",
				event.GroupID, event.CustomerID, event.OrderTotal)

			success := true

			if success {
				msgs := make(chan amqp.Delivery, 1)
				msgs <- d
				close(msgs)

				go func() {
					if err := payment.Publish(payment.PublishEvent{
						Ch:   ch,
						Msgs: msgs,
					}); err != nil {
						log.Printf("Failed to publish payment processed event: %s", err)
					} else {
						log.Printf("Payment processed event published for Order ID: %s", event.GroupID)
					}
				}()
			}
		}
	}()

	log.Printf(" Waiting for messages")
	<-forever
}
