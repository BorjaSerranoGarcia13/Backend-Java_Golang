# E-Commerce Application

This is an e-commerce application built with Java, JavaScript, Spring Boot, and Maven. The application allows users to browse products, add them to a shopping cart, and place orders. Data is initialized from JSON files.

## Table of Contents

- [Technologies](#technologies)
- [Installation](#installation)
- [Usage](#usage)

## Technologies

This project is implemented with the following technologies:

- Java: The backend is implemented in Java.
- JavaScript: Used for frontend scripting.
- Spring Boot: The application uses Spring Boot for creating stand-alone, production-grade Spring based applications.
- Maven: Maven is used as the build tool for this project.
- Jackson: Used for JSON object mapping.
- JWT: JSON Web Tokens are used for securely transmitting information between parties as a JSON object.
- Docker: Docker is used to create, deploy, and run applications by using containers. In this project, Docker is used to run the database and the application in separate containers, ensuring that the application runs the same regardless of the environment.
- Spring Security: Spring Security is used in this project to protect the application endpoints and ensure that only authenticated users can access certain resources.

## Installation

Follow these steps to install and run the project:

1. Clone the repository: `git clone https://github.com/BorjaSerranoGarcia13/backend-java.git`
2. Make sure you have Docker installed. You can check by running `docker --version`.
3. Navigate to the project directory: `cd backend-java.git`.
4. Execute the `run-docker` bash script: `bash run-docker.sh`. This script sets up the necessary Docker containers for the application.
5. Start all services with Docker: `docker-compose up`.

Now, the application and all its services should be running. You can access the application at `http://localhost:8080`.

## Usage

This e-commerce application provides a user interface for browsing products, adding them to a shopping cart, and placing orders. It also exposes a REST API for interacting with the application programmatically.

Many of the API endpoints require authentication or admin permissions. To access these, you need to log in with a valid user to generate a valid JWT Token. 
This token is used to authenticate your requests. You can log out to end your session and switch users if needed. 
The `DataGenerator` class creates a new list of users in the database each time the application starts. 
The generated users are stored in a file named `users.json`.

The application generates a list of users each time it starts, with usernames ranging from `username1` to `username50`. Users with usernames from `username1` to `username10` have admin roles, while users with usernames from `username11` to `username50` are regular users.


Here are some example credentials you can use:

- Admin access: `username: username1, password: 123`
- Registered user access: `username: username11, password: 123`

Please note that the actual usernames and passwords may vary as they are generated anew each time the application starts. Always refer to the `users.json` file for the current list of users.

### User Interface

The user interface allows users to browse products, add them to a shopping cart, and place orders. Users can register and log in to access their personal shopping cart and order history.
To access the Swagger documentation through the user interface, you can navigate to the following URL in your browser:
#### Request
```
[GET] http://localhost:8080/swagger-ui.html
```
Please refer to DOCUMENTATION.md for a complete list of endpoints and their usage.

### REST API

The application's REST API provides endpoints for managing users, products, and orders. You can use a tool like Insomnia to send HTTP requests to these endpoints. Here are some examples:

- `GET /api/v1/products`: Fetches a list of all products.
- `POST /api/v1/users`: Creates a new user.
- `POST /api/v1/orders`: Places a new order.

To access the Swagger documentation in JSON format, you can navigate to the following URL in your browser/tool:
#### Request
```
[GET] http://localhost:8080/v3/api-docs
```

Please refer to DOCUMENTATION.md for a complete list of endpoints and their usage.

### Using Insomnia

To use Insomnia with this application, you need to be logged in as an admin to create a token. This token is used to authenticate and authorize your requests. Here's how you can do it:

1. Send a `POST` request to `/api/auth/login` with the admin credentials in the request body. The response will include the token.
2. In Insomnia, set up an environment variable for the token.
3. For each request, set the `Authorization` header to `Bearer <your-token>`.

Please note that the token expires after 10 minutes. You will need to log in again to get a new token once the old one expires.
The application initializes data from JSON files located in `src/main/resources/data`. The `DataInitializer` class reads these files and populates the database on startup.
