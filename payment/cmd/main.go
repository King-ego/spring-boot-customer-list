package main

import (
	"encoding/json"
	"log"
	"time"

	"github.com/streadway/amqp"
)

type OrderCreatedEvent struct {
	GroupID     string  `json:"groupId"`
	CustomerID  string  `json:"customerId"`
	OrderStatus string  `json:"orderStatus"`
	OrderTotal  float64 `json:"orderTotal"`
}

func connectToRabbitMQ() (*amqp.Connection, error) {
	var conn *amqp.Connection
	var err error

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
		log.Fatal("Failed to open a channel : %s", err)
	}
	defer func(ch *amqp.Channel) {
		err := ch.Close()
		if err != nil {
			log.Printf("Error closing channel: %s", err)
		}
	}(ch)

	queue, err := ch.QueueDeclare(
		"customer-queue",
		true,
		false,
		false,
		false,
		nil,
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
		}
	}()

	log.Printf(" [*] Waiting for messages. To exit press CTRL+C")
	<-forever
}
