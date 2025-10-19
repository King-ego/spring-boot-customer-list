FROM golang:1.24.6

WORKDIR /src/challenge_english

COPY go.mod go.sum ./

RUN go mod download

COPY . .

EXPOSE 8097

RUN go build -o main cmd/main.go

CMD ["./main"]