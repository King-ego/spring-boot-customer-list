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

func GetChallengeCron() *cron.Cron {
	c := cron.New()

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
	if apiKey == "" {
		return fmt.Errorf("HUGGING_KEY não está setado")
	}

	modelURL := "https://router.huggingface.co/v1/chat/completions"

	payload := map[string]interface{}{
		"model": "zai-org/GLM-4.6-FP8:zai-org",
		"messages": []map[string]interface{}{
			{
				"role":    "user",
				"content": "What is the capital of France?",
			},
		},
		"stream": false,
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
	if res == nil {
		return fmt.Errorf("resposta HTTP é nil")
	}
	defer func() {
		_ = res.Body.Close()
	}()

	data, err := io.ReadAll(res.Body)
	if err != nil {
		return fmt.Errorf("falha ao ler resposta: %w", err)
	}

	if res.StatusCode == http.StatusNotFound {
		return fmt.Errorf("modelo não encontrado (404). verifique o slug '%s' no Hugging Face Hub. resposta: %s", modelURL, string(data))
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
