# Auth System Tool

Auth Tool is a Spring Boot application written in Kotlin that provides basic user authentication functionality. It includes endpoints for user sign up and sign in.

## Technologies Used

- Kotlin
- Java 17 or newer for execution
- Spring Boot
- Gradle
- Argon2 for password encryption
- Swagger for API documentation

## Application Structure

The application is structured into the following main directories:

- `src/main/kotlin/app/tools/auth/model`: Contains the data model for the application. The `User` class represents a user in the system.
- `src/main/kotlin/app/tools/auth/controller`: Contains the controllers for handling HTTP requests. The `HomeController` class handles requests for user sign up and sign in.
- `src/main/resources`: Contains application properties files.

## Setup

To run this application, you need to have Kotlin and Java installed on your machine.

1. Clone the repository: `git clone https://github.com/sievericcardo/auth-tools.git`
2. Navigate to the project directory: `cd auth-tools`
3. Build the project: `./gradlew build`
4. Run the application: `java -jar build/libs/auth-tool-0.1.jar`

Note that to run the application you need to export the following environment variables:

- `DB_URL=jdbc:postgresql://<host>:<port>/<path>`
- `DB_USERNAME=<db_username>`
- `DB_PASSWORD=<db_password`
- `DB_DRIVER=org.postgresql.Driver`
- `DB_DIALECT=org.hibernate.dialect.PostgreSQLDialect`

changing the placeholders with the actual values and `postgres` with the actual database used.

## API Endpoints

- `GET /api/status`: Returns the status of the application.
- `POST /api/signup`: Signs up a new user. Requires a JSON body with `userRequest` containing `username`, `email`, and `password` and `passwordCheck` with the same `password`.
- `POST /api/signin`: Signs in a user. Requires a JSON body with `userRequest` containing `username` and `password`.

## Swagger UI

You can access the Swagger UI at `http://localhost:8081/swagger-ui.html` to interact with the API and view its documentation.

## Security

Passwords are encrypted using the Argon2 algorithm. The configuration for Argon2 can be found in the `application.properties` file.

## Database

The application uses a database for storing user data. The database connection details are configured in the `application.properties` file using the environment variables defined.

## Contributing

Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.
