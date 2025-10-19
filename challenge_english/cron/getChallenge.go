package cron

import (
	"fmt"
	"time"

	"github.com/robfig/cron/v3"
)

func GetChallengeCron() *cron.Cron {
	c := cron.New()

	// Schedule a job to run every minute
	if _, err := c.AddFunc("@every 10m", func() {
		fmt.Println("This job runs every minute:", time.Now())
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
