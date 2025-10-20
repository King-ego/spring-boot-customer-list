package cron

import (
	"bytes"
	"encoding/json"
	"fmt"
	"io"
	"net/http"
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
	apiKey := os.Getenv("HUGGING_KEY")
	fmt.Println("OpenAI API key:", apiKey)
	if apiKey == "" {
		return "Não encontrado", fmt.Errorf("OPENAI_KEY não setado")
	}

	reqBody := openAIRequest{
		Model: "gpt-4o-mini",
		Input: "Write a short bedtime story about a unicorn.",
	}

	b, err := json.Marshal(reqBody)
	if err != nil {
		return "", fmt.Errorf("marshal request: %w", err)
	}

	req, err := http.NewRequest(http.MethodPost, "https://api-inference.huggingface.co/models/facebook/bart-large-cnn", bytes.NewReader(b))
	if err != nil {
		return "", fmt.Errorf("criar request: %w", err)
	}
	req.Header.Set("Content-Type", "application/json")
	req.Header.Set("Authorization", "Bearer "+apiKey)

	client := &http.Client{Timeout: 15 * time.Second}
	resp, err := client.Do(req)
	if err != nil {
		return "", fmt.Errorf("request failed: %w", err)
	}
	defer func(Body io.ReadCloser) {
		err := Body.Close()
		if err != nil {

		}
	}(resp.Body)

	body, _ := io.ReadAll(resp.Body)
	if resp.StatusCode < 200 || resp.StatusCode >= 300 {
		return "", fmt.Errorf("status %d: %s", resp.StatusCode, string(body))
	}

	// decodifica e imprime a resposta para inspeção
	var out map[string]interface{}
	if err := json.Unmarshal(body, &out); err != nil {
		return "", fmt.Errorf("decodificar resposta: %w", err)
	}
	pretty, _ := json.MarshalIndent(out, "", "  ")
	fmt.Printf("OpenAI response:\n%s\n", pretty)

	return apiKey, nil
}
