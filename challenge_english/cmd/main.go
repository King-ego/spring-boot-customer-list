package main

import (
	"log"

	"github.com/gin-gonic/gin"
)

func main() {
	server := gin.Default()

	if err := server.Run(":8097"); err != nil {
		log.Fatalf("failed to run server: %v", err)
	}
}
