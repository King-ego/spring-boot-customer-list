package lambdas

import (
	"context"
	"fmt"

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

	// Lambda pode retornar erro via FunctionError ou payload com dados
	if resp.FunctionError != nil {
		return fmt.Errorf("lambda returned function error: %s", *resp.FunctionError)
	}

	if resp.Payload != nil {
		// imprime resposta do lambda (opcional)
		fmt.Println("Lambda response:", string(resp.Payload))
	}

	return nil
}
