# Getting Started

## Project Structure

This project structure is designed to be scalable, maintainable, and follows Spring Boot best practices. Here's a brief
explanation of each directory and file:

1. `src/main/java`: Contains all the Java source code.
    - `controller/`: REST API controllers.
    - `dto/`: Data Transfer Objects.
    - `exception/`: Custom exception classes.
    - `model/`: Entity classes.
    - `repository/`: Data access layer interfaces.
    - `service/`: Business logic layer.

2. `src/main/resources`: Contains application properties and other resources.
    - `application.yml`: Main configuration file.
    - `application-dev.yml` and `application-prod.yml`: Environment-specific configurations.

3. `src/test`: Contains all test classes and resources.

4. `Dockerfile`: For containerizing the application.

5. `build.gradle`: Gradle build configuration.

6. `README.md`: Project documentation.

# Run the Swagger UI

Hers is the link to swagger ui editor once you have build and run the application in dev mode:

```
http://localhost:8080/swagger-ui/index.html
```


# Set Database Connection Details:
For deployment purpose, you can change the ```.env``` file set the database connection details.
you need to include these values:


```shell
JDBC_URL=jdbc:postgresql://<ip>:<port>/<database>
DB_USERNAME=<username>
DB_PASSWORD=<password>
```