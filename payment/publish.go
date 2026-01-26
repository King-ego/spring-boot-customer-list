package payment

import "github.com/streadway/amqp"

type PublishEvent struct {
	Ch   *amqp.Channel
	Msgs <-chan amqp.Delivery
}

func Publish(event PublishEvent) error {
	var err error
	for d := range event.Msgs {
		if err := event.Ch.Publish(
			"payment-result-exchange", // ✅ Exchange de resposta
			"payment.processed",       // ✅ Routing key
			false,
			false,
			amqp.Publishing{
				ContentType: "application/json",
				Body:        d.Body,
				Headers: amqp.Table{
					"__TypeId__": "com.diego.list.customers.fila.exchange.event.PaymentProcessedEvent",
				},
			},
		); err != nil {
			return err
		}
		return nil
	}

	return err
}
