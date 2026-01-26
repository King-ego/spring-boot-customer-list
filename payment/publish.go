package payment

import (
	"encoding/json"
	"log"

	"github.com/streadway/amqp"
)

type PublishEvent struct {
	Ch             *amqp.Channel
	ProcessedEvent ProcessedEvent
}

func Publish(event PublishEvent) error {
	paymentEventJSON, err := json.Marshal(event.ProcessedEvent)

	if err != nil {
		log.Printf("Error encoding payment event: %s", err)
		return err
	}

	if err := event.Ch.Publish(
		"payment-result-exchange",
		"payment.processed",
		false,
		false,
		amqp.Publishing{
			ContentType: "application/json",
			Body:        paymentEventJSON,
			Headers: amqp.Table{
				"__TypeId__": "com.diego.list.customers.fila.exchange.event.PaymentProcessedEvent",
			},
		},
	); err != nil {
		return err
	}

	return nil

}
