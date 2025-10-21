package lambdas

import (
	"github.com/aws/aws-sdk-go-v2/service/lambda"
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
