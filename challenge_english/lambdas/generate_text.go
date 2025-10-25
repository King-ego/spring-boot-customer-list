package lambdas

import (
	"context"
	"encoding/json"
	"fmt"
	"os"
	"time"

	"github.com/aws/aws-sdk-go-v2/service/lambda"
	"github.com/aws/aws-sdk-go-v2/service/lambda/types"
)

type RunChallenge struct {
	client  *lambda.Client
	message string
}

func NewChallengeJob(client *lambda.Client, message string) *RunChallenge {
	return &RunChallenge{
		client:  client,
		message: message,
	}
}

func (cj *RunChallenge) RunChallengeJob() {
	var client = cj.client
	var message = cj.message

	fmt.Println(message)

	lambdaPayload := map[string]interface{}{
		"event": "cron_run",
		"when":  time.Now().Format(time.RFC3339),
	}
	pb, _ := json.Marshal(lambdaPayload)

	if name := os.Getenv("LAMBDA_NAME"); name != "" {

		if err := invokeLambda(context.Background(), client, name, pb); err != nil {
			fmt.Printf("erro ao invocar lambda: %v\n", err)

		} else {
			fmt.Println("Lambda invocado com sucesso")
		}

	} else {
		fmt.Println("LAMBDA_NAME não configurado; pulando invocação")
	}
}

func invokeLambda(ctx context.Context, client *lambda.Client, functionName string, payload []byte) error {
	if client == nil {
		return fmt.Errorf("lambda client não inicializado")
	}

	input := &lambda.InvokeInput{
		FunctionName:   &functionName,
		Payload:        payload,
		InvocationType: types.InvocationTypeRequestResponse, // aguardando resposta
	}

	resp, err := client.Invoke(ctx, input)

	if err != nil {
		return fmt.Errorf("invoke failed: %w", err)
	}

	if resp.FunctionError != nil {
		return fmt.Errorf("lambda returned function error: %s", *resp.FunctionError)
	}

	if resp.Payload != nil {
		fmt.Println("Lambda response:", string(resp.Payload))
	}

	return nil
}
