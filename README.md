# Customer List Application

A simple Spring Boot application for managing and listing customers/users.

## 📋 Overview

This is a demo Spring Boot application that provides basic functionality for customer management. The application is built using Java 21 and Spring Boot 3.5.4, offering RESTful APIs for customer operations.

## 🚀 Features

- Customer listing functionality
- RESTful API endpoints
- Health check endpoint
- Spring Boot DevTools for development
- Maven-based project structure

## 🛠️ Technologies Used

- **Java 21**
- **Spring Boot 3.5.4**
- **Spring Web**
- **Maven** for dependency management
- **Spring Boot DevTools** for development productivity

## 📦 Prerequisites

Before running this application, make sure you have:

- Java 21 or higher installed
- Maven 3.6+ installed
- Your favorite IDE (IntelliJ IDEA, Eclipse, VS Code, etc.)

## 🏃‍♂️ Getting Started

### Clone the Repository

```bash
git clone <repository-url>
cd list.customers
```

### Build the Application

```bash
./mvnw clean compile
```

### Run the Application

```bash
./mvnw spring-boot:run
```

Or alternatively:

```bash
java -jar target/list.customers-0.0.1-SNAPSHOT.jar
```

The application will start on `http://localhost:8080`

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

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📝 License

This project is a demo application for learning purposes.

## 📞 Contact

For questions or support, please reach out to the development team.

---

*This is a demo project created for educational purposes using Spring Boot framework.*
