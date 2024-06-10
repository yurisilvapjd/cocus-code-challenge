# Code Challenge for COCUS

## Features
- **Spring Boot**: Simplifies the development of web applications.
- **Java 17**: Provides modern language features and performance improvements.
- **Gradle**: Efficient build automation tool for dependency management and project configuration.
- **Springdoc-OpenAPI**: Automates the generation of OpenAPI documentation.
- **Swagger-UI**: Offers a user-friendly interface for API exploration.
- **JUnit 5**: A powerful testing framework for Java applications.
- **Mockito**: A mocking framework for unit tests.

## Architecture
The application is implemented using a layered architecture, driven by interfaces to enforce SOLID principles:

- **Single Responsibility Principle**: Each class or module has only one reason to change, adhering to a single responsibility.
- **Open-Closed Principle**: Classes are open for extension but closed for modification.
- **Liskov Substitution Principle**: Subtypes are replaceable by their base types.
- **Interface Segregation Principle**: Clients are not forced to depend on interfaces they do not use.
- **Dependency Inversion Principle**: High-level modules do not depend on low-level modules; both depend on abstractions.

Layered architecture is chosen for its clear separation of concerns, promoting maintainability, scalability, and ease of testing.

## Getting Started

### Prerequisites
- Java 17
- Gradle 7+
- Git

## Installation

#### 1. Clone the repository:
```bash
git clone https://github.com/yourusername/cocus-code-challenge.git
cd cocus-code-challenge
```

#### 2. Build the project:
```bash
./gradlew clean build
```

#### 3. Run the application:
```bash
./gradlew bootRun
```

## API Documentation
Once the application is running, you can access the API documentation at:
```
http://localhost:8080/swagger-ui.html
```

## Running Tests
To execute the test suite, run:
```bash
./gradlew test
```
