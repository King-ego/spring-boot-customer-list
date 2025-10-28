FROM golang:1.24.6


WORKDIR /src/payment

COPY go.mod go.sum ./

RUN go mod download

COPY . .

EXPOSE 8787

RUN go build -o main cmd/main.go

CMD ["./main"]