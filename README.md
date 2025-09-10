# E-Commerce Order Management System

A Spring Boot-based order management system for e-commerce applications with product catalog, customer management, order processing, and simulated payment integration.

## 📋 Overview

This is a demo Spring Boot application that provides basic functionality for customer management. The application is built using Java 21 and Spring Boot 3.5.4, offering RESTful APIs for customer operations.

## Features

- **Product Management**: CRUD operations for products with inventory tracking
- **Customer Management**: Complete customer profile and address management
- **Order Processing**: Order creation, status tracking, and inventory validation
- **Payment Integration**: Simulated payment gateway with webhook support
- **RESTful API**: Comprehensive API endpoints for all operations
- **Data Validation**: Robust input validation and error handling
- **Testing**: Comprehensive unit and integration tests


## Tech Stack

- **Java 21**
- **Spring Boot 3.2.0**
- **Spring Data JPA**
- **H2 Database** (Development)
- **PostgreSQL 17** (Production)
- **Maven**
- **Docker & Docker Compose**
- **JUnit 5 & Mockito**

## 📦 Prerequisites

Before running this application, make sure you have:

- Java 21 or higher installed
- Maven 3.6+ installed
- Your favorite IDE (IntelliJ IDEA, Eclipse, VS Code, etc.)

## 🏃‍♂️ Getting Started

### Clone the Repository

```bash
git clone git@github.com:King-ego/spring-boot-customer-list.git
cd list.customers
```

### Run the Application

```bash
docker compose up --build
```

The application will start on `http://localhost:8079`

## 📡 API Endpoints

### Health Check
- **GET** `/health`
  - Description: Check application health status
  - Query Parameters:
    - `name` (optional): Name to include in greeting (default: "World")
  - Example: `GET /health?name=Diego`
  - Response: `Hello, Diego!`

### Customer Operations
*Additional customer endpoints will be implemented here*

## 🧪 Running Tests

Execute the test suite:

```bash
./mvnw test
```

## 📁 Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/diego/list/customers/
│   │       ├── Application.java          # Main application class
│   │       └── controller/
│   │           └── HealthController.java # Health check controller
│   └── resources/
│       ├── application.properties        # Application configuration
│       ├── static/                      # Static resources
│       └── templates/                   # Template files
└── test/
    └── java/
        └── com/diego/list/customers/
            └── ApplicationTests.java     # Application tests
```

## 🔧 Configuration

The application uses default Spring Boot configurations. You can modify settings in `src/main/resources/application.properties`.

## 📝 License

This project is a demo application for learning purposes.

## 📞 Contact

For questions or support, please reach out to the development team.

---

*This is a demo project created for educational purposes using Spring Boot framework.*
