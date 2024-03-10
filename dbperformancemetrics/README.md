# DB Performance Metrics

This project is a Java application developed with the Spring Boot framework and managed with Maven. Its primary objective is to conduct performance testing on two different databases: MongoDB and Oracle.

## Table of Contents

- [Repository](#repository)
- [Docker](#docker)
- [Insomnia](#insomnia)


## Repository

Follow these steps to install and run the project:

1. Clone the repository: `git clone https://github.com/BorjaSerranoGarcia13/backend-java.git`
2. Make sure you have Docker and Docker Compose installed. You can check by running `docker --version`.
3. Open your terminal. Navigate to the project directory (the directory containing the `docker-compose.yml` file).
5. Run the command `docker-compose up`.
 
This will start all the services defined in the `docker-compose.yml` file. Docker Compose will pull the necessary images (if they're not already locally available), build your services, and then start them.
Now, the application and all its services should be running. You can access the application at `http://localhost:8080`.

## Docker

### Step 1: Install Docker Compose

Follow the official installation guide [here](https://docs.docker.com/compose/install/).

### Step 2: Understand the Docker Compose file

The `docker-compose.yml` file in this project defines two services: `mongodb_service` and `oracle_db_service`.

### Step 3: Run Docker Compose

Navigate to the directory containing the `docker-compose.yml` file in your terminal. Run the command `docker-compose up` to start the containers.

### Step 4: Verify the containers are running

You can use the command `docker ps` to list all running containers.

### Step 5: Interact with the databases

With the containers running, you can now interact with the databases.

### Step 6: Stop the containers

When you're done, you can stop the containers by running `docker-compose down` in the same directory as the `docker-compose.yml` file.
 
##### This will start all the services defined in the `docker-compose.yml` file. Docker Compose will pull the necessary images (if they're not already locally available), build your services, and then start them.

## Insomnia
This project includes an Insomnia export file, which contains all the predefined HTTP requests for interacting with the API.

To use it, follow these steps:

1. Install [Insomnia](https://insomnia.rest/download) if you haven't already.
2. Open Insomnia and select "Import/Export" in the menu.
3. Select "Import Data" > "From File".
4. Navigate to the `insomnia-export.json` file in the `insomnia` directory of this project and click "Open".

You should now see all the predefined HTTP requests in Insomnia, ready to be used.

## **Please refer to DOCUMENTATION.md for a complete list of endpoints and their usage.**

