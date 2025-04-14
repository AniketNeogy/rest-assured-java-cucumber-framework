# REST Assured Java API Testing Framework for PetStore API

A comprehensive, robust, and scalable REST Assured-based framework for testing the [Swagger PetStore API](https://petstore.swagger.io/).

## Features

- **Modular Design**: Clean separation of concerns with model, client, and test classes
- **POJO Serialization/Deserialization**: Using Jackson for seamless object mapping
- **Lombok Integration**: Reduces boilerplate code for model classes
- **Extensive Logging**: Detailed logging of requests and responses using Log4j2
- **Reporting**: Comprehensive HTML reports using ExtentReports
- **Parallel Execution**: Support for concurrent test execution
- **TestNG Integration**: Leveraging TestNG for powerful test organization and execution
- **Data Generation**: Random test data generation using JavaFaker
- **Fluent Assertions**: Enhanced assertions with AssertJ

## Introduction to RestAssured for Beginners

RestAssured is a Java library that simplifies testing RESTful APIs by providing a fluent, easy-to-use interface for HTTP requests and assertions. It handles JSON/XML parsing, authentication, and more, making it ideal for beginners. This project uses RestAssured (version 5.3.1) to send requests, verify responses, and integrate with TestNG for structured testing.

## Project Structure

```
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── petstore
│   │   │           └── api
│   │   │               ├── client
│   │   │               │   ├── PetClient.java
│   │   │               │   ├── RestClient.java
│   │   │               │   ├── StoreClient.java
│   │   │               │   └── UserClient.java
│   │   │               ├── config
│   │   │               │   └── ApiConfig.java
│   │   │               ├── constants
│   │   │               │   ├── Endpoints.java
│   │   │               │   └── Status.java
│   │   │               └── model
│   │   │                   ├── ApiResponse.java
│   │   │                   ├── Category.java
│   │   │                   ├── Order.java
│   │   │                   ├── Pet.java
│   │   │                   ├── Tag.java
│   │   │                   └── User.java
│   │   └── resources
│   │       └── log4j2.xml
│   └── test
│       ├── java
│       │   └── com
│       │       └── petstore
│       │           └── api
│       │               ├── base
│       │               │   └── TestBase.java
│       │               ├── listeners
│       │               │   └── TestListener.java
│       │               ├── tests
│       │               │   ├── pet
│       │               │   │   └── PetApiTests.java
│       │               │   ├── store
│       │               │   │   └── StoreApiTests.java
│       │               │   └── user
│       │               │       └── UserApiTests.java
│       │               └── utils
│       │                   └── TestUtils.java
│       └── resources
│           ├── config
│           │   └── config.properties
│           └── testng.xml
```

## Setup and Installation

### Prerequisites
- Java 11 or newer (ensure it's installed and configured in your PATH)
- Maven 3.6.0 or newer (for dependency management and test execution)
- An IDE like IntelliJ or Eclipse for development

### Step-by-Step Installation
1. Clone the repository:
   ```bash
git clone <repository-url>
   cd rest-assured-java-integration-testing
   ```

2. Install dependencies:
   ```bash
   mvn clean install -DskipTests
   ```

3. Configure environment-specific settings:
   Edit `src/test/resources/config/config.properties` to update API URLs or timeouts if needed (e.g., for local testing).

This process ensures a smooth setup, even if you're new to Java or Maven.

## Configuration

The framework can be configured through the `src/test/resources/config/config.properties` file:

```properties
# API Configuration
api.base.url=https://petstore.swagger.io/v2
api.request.timeout=10000
api.log.all.requests=true
api.log.all.responses=true

# Report Configuration
report.name=PetStore API Test Report
report.title=PetStore Swagger API Test Report
report.output.dir=test-output/reports
```

## How to Run Tests

### Running All Tests
Use the following command to execute the entire test suite:
```bash
mvn clean test
```

### Running Specific Test Groups or Classes
- For a specific group (e.g., pet-related tests):
  ```bash
  mvn clean test -Dgroups=pet
  ```
- For a specific test class (e.g., PetApiTests):
  ```bash
  mvn clean test -Dtest=PetApiTests
  ```

### Tips for Beginners
- If you're new to Maven, this command compiles code, runs tests, and generates reports. Check the console for output or errors.
- Ensure your API endpoint (e.g., in config.properties) is accessible; otherwise, tests may fail due to network issues.

## Reporting and Logging Infrastructure

### Reporting
This project uses ExtentReports for detailed HTML reports:
- **How it Works**: Tests generate reports that include pass/fail status, error messages, and execution times.
- **How to View Reports**: After running tests, find the HTML reports in the `test-output/reports` directory (configured in config.properties). Open the latest file (e.g., `PetStoreAPIReport_YYYY-MM-DD_HH-mm-ss.html`) in a web browser for an interactive view.
- Or you can try the below command on your terminal 
```bash 
start test-output\reports\<PetStoreAPIReport_YYYY-MM-DD_HH-mm-ss.html>
```                                
- **Key Features**: Reports are customized with system info like the API base URL, and they integrate with the TestListener for real-time updates.

### Logging
Logging is handled by Log4j2 for comprehensive monitoring:
- **How it Works**: It logs requests, responses, and test events at different levels (e.g., INFO, ERROR). This helps in debugging and tracing issues.
- **How to View Logs**: Logs are output to the console during test runs and saved in the `logs` directory as rolling files. Check the latest log file for details, or configure log levels in `log4j2.xml` for more verbosity.
- **Design Choice**: We use SLF4J with Log4j2 for flexibility, allowing easy switching of logging implementations if needed.

## Design Choices and Patterns

This framework incorporates several key design patterns and choices to ensure maintainability and scalability:

- **Modular Design**: The codebase separates concerns into clients (for API calls), models (for data objects), and tests (for verification), following the Single Responsibility Principle. This makes it easy for new developers to navigate and extend.

- **POJO with Lombok**: Model classes (e.g., Pet, User) use Plain Old Java Objects (POJOs) with Lombok annotations to reduce boilerplate, making code cleaner and faster to write.

- **Retry Pattern for Flaky Tests**: Implemented in TestUtils.executeWithRetry, this handles intermittent API issues (e.g., 404s due to latency), improving test reliability without overcomplicating individual tests.

- **Dependency Injection via Clients**: API clients (e.g., PetClient) encapsulate REST Assured logic, allowing tests to focus on behavior rather than implementation details.

- **TestNG Annotations and Lifecycle Management**: Uses @BeforeSuite, @AfterMethod, etc., for setup and teardown, ensuring resources are managed efficiently.

These patterns were chosen to promote readability, reusability, and ease of maintenance, especially in a team environment.

## Best Practices

This framework implements several best practices:

1. **Separation of Concerns**: Clear separation between API clients, models, and tests
2. **Reusable Components**: Common functionality extracted into reusable components
3. **Proper Error Handling**: Comprehensive error handling with detailed error messages
4. **Consistent Logging**: Standardized logging throughout the framework
5. **Clean Test Design**: Tests follow the Arrange-Act-Assert pattern
6. **Parallel Execution**: Support for concurrent test execution
7. **Configuration Management**: Externalized configuration for environment-specific settings

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Create a pull request

## License

This project is licensed under the MIT License - see the LICENSE file for details. 