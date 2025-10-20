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

/*
	type openAIRequest struct {
		Model string      `json:"model"`
		Input interface{} `json:"input"`
	}
*/
func GetChallengeCron() *cron.Cron {
	c := cron.New()

	// Schedule a job to run every minute
	if _, err := c.AddFunc("@every 1m", func() {
		fmt.Println("This job runs every minute:", time.Now())
		err := callOpenAi()
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

func callOpenAi() error {
	apiKey := os.Getenv("HUGGING_KEY")
	if apiKey == "" {
		fmt.Println("Erro: HUGGING_KEY não está setado")
		return fmt.Errorf("HUGGING_KEY não está setado")
	}

	// Aqui o modelo do Hugging Face, não da OpenAI
	/*modelURL := "https://api-inference.huggingface.co/models/facebook/bart-large-cnn"
	 */
	modelURL := "https://api-inference.huggingface.co/models/gpt2"

	// Para geração de texto, usamos parâmetros diferentes
	payload := map[string]interface{}{
		"inputs": "Escreva uma história curta de ninar sobre um unicórnio mágico que sonha em voar:",
	}

	body, _ := json.Marshal(payload)

	req, err := http.NewRequest(http.MethodPost, modelURL, bytes.NewBuffer(body))
	if err != nil {
		panic(err)
	}

	req.Header.Set("Content-Type", "application/json")
	req.Header.Set("Authorization", "Bearer "+apiKey)

	res, err := http.DefaultClient.Do(req)
	if err != nil {
		panic(err)
	}
	defer func(Body io.ReadCloser) {
		err := Body.Close()
		if err != nil {

		}
	}(res.Body)

	data, _ := io.ReadAll(res.Body)
	fmt.Println("Status:", res.Status)
	fmt.Println("Resposta:", string(data))
	return nil
}
