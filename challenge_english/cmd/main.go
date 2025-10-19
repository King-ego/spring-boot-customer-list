package main

import (
	"challenge_english/cron"
	"log"

	"github.com/gin-gonic/gin"
)

func main() {
	server := gin.Default()

	cron.GetChallengeCron()

	if err := server.Run(":8097"); err != nil {
		log.Fatalf("failed to run server: %v", err)
	}
}
