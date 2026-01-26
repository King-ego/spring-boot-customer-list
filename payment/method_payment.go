package payment

type ProcessedEvent struct {
	GroupID     string `json:"groupId"`
	CustomerID  string `json:"customerId"`
	Status      string `json:"status"`
	PaymentId   string `json:"paymentId"`
	PaymentDate string `json:"paymentDate"`
}

type OrderCreatedEvent struct {
	GroupID     string  `json:"groupId"`
	CustomerID  string  `json:"customerId"`
	OrderStatus string  `json:"orderStatus"`
	OrderTotal  float64 `json:"orderTotal"`
}

func Processed(event OrderCreatedEvent) (ProcessedEvent, error) {

	return ProcessedEvent{
		GroupID:     event.GroupID,
		CustomerID:  event.CustomerID,
		Status:      "APPROVED",
		PaymentId:   "PAY-123456",
		PaymentDate: "2024-01-01T12:00:00Z",
	}, nil
}
