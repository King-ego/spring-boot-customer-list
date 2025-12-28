package com.diego.list.customers;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@EnableRabbit
@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.diego.list.customers.repository")
@EnableRedisRepositories(basePackages = "com.diego.list.customers.redis")
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}


