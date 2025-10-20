package cron

import (
	"fmt"
	"os"
	"time"

	"github.com/robfig/cron/v3"
)

type openAIRequest struct {
	Model string      `json:"model"`
	Input interface{} `json:"input"`
}

func GetChallengeCron() *cron.Cron {
	c := cron.New()

	// Schedule a job to run every minute
	if _, err := c.AddFunc("@every 1m", func() {
		fmt.Println("This job runs every minute:", time.Now())
		_, err := callOpenAi()
		if err != nil {
			fmt.Printf("Error calling OpenAI: %v\n", err)
		}
	}); err != nil {
		fmt.Printf("failed to add 1m job: %v\n", err)
	}

	// Schedule a job to run at a specific time (e.g., 2:30 PM every day)
	if _, err := c.AddFunc("30 14 * * *", func() {
		fmt.Println("This job runs at 2:30 PM:", time.Now())
	}); err != nil {
		fmt.Printf("failed to add 2:30 PM job: %v\n", err)
	}

	c.Start()

	return c
}

func callOpenAi() (string, error) {
	apiKey := os.Getenv("OPENAI_KEY")
	fmt.Println("OpenAI API key:", apiKey)
	if apiKey == "" {
		return "Não encontrado", fmt.Errorf("OPENAI_KEY não setado")
	}

	reqBody := openAIRequest{
		Model: "gpt-5",
		Input: "Write a short bedtime story about a unicorn.",
	}

	return apiKey, nil
}
