package cron

import (
	"bytes"
	"context"
	"encoding/json"
	"fmt"
	"io"
	"net/http"
	"os"
	"time"

	"github.com/aws/aws-sdk-go-v2/config"
	"github.com/aws/aws-sdk-go-v2/service/lambda"
	"github.com/robfig/cron/v3"
)

type Message struct {
	Role    string `json:"role"`
	Content string `json:"content"`
}

type Payload struct {
	Model    string    `json:"model"`
	Messages []Message `json:"messages"`
	Stream   bool      `json:"stream"`
}

func GetChallengeCron() *cron.Cron {
	c := cron.New()
	cfg, err := config.LoadDefaultConfig(context.Background())
	if err != nil {
		fmt.Printf("erro ao carregar config AWS: %v\n", err)
	}

	lambdaClient := lambda.NewFromConfig(cfg)

	// Schedule a job to run every minute
	if _, err := c.AddFunc("@every 1m", func() {
		fmt.Println("This job runs every minute:", time.Now())
		if err := callOpenAi(); err != nil {
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

func callOpenAi() error {
	apiKey := os.Getenv("HUGGING_KEY")
	modelURL := os.Getenv("HUGGING_MODEL")
	if apiKey == "" || modelURL == "" {
		return fmt.Errorf("HUGGING_KEY não está setado")
	}

	payload := Payload{
		Model: "zai-org/GLM-4.6-FP8:zai-org",
		Messages: []Message{
			{Role: "user", Content: "Qual é a capital da França?"},
		},
		Stream: false,
	}

	bodyBytes, err := json.Marshal(payload)
	if err != nil {
		return fmt.Errorf("falha ao serializar payload: %w", err)
	}

	req, err := http.NewRequest(http.MethodPost, modelURL, bytes.NewReader(bodyBytes))
	if err != nil {
		return fmt.Errorf("falha ao criar requisição: %w", err)
	}
	req.Header.Set("Content-Type", "application/json")
	req.Header.Set("Authorization", "Bearer "+apiKey)

	client := &http.Client{Timeout: 50 * time.Second}
	res, err := client.Do(req)

	if err != nil {
		return fmt.Errorf("falha na requisição HTTP: %w", err)
	}

	defer func() {
		_ = res.Body.Close()
	}()

	data, err := io.ReadAll(res.Body)
	if err != nil {
		return fmt.Errorf("falha ao ler resposta: %w", err)
	}

	if res.StatusCode >= 400 {
		return fmt.Errorf("requisição retornou status %d: %s", res.StatusCode, string(data))
	}

	var parsed []map[string]interface{}
	if err := json.Unmarshal(data, &parsed); err == nil && len(parsed) > 0 {
		if gen, ok := parsed[0]["generated_text"].(string); ok {
			fmt.Println("Resposta gerada:", gen)
			return nil
		}
	}

	fmt.Println("Status:", res.Status)
	fmt.Println("Resposta (raw):", string(data))
	return nil
}
