# ToDo List Application with Dropwizard and MySQL

## Project Overview
This project is a **ToDo List Application** built using the Dropwizard framework and MySQL database. The application provides RESTful APIs for task management, including creating, updating, retrieving, and deleting tasks. The project is containerized using Docker and orchestrated with Docker Compose.

## Features
- **Create a Task**: Add a new task to the ToDo list.
- **Retrieve a Task**: Get details of a specific task.
- **List All Tasks**: Fetch a list of all tasks.
- **Delete a Task**: Remove a task from the ToDo list.
- **Update a Task**: Modify an existing task.

## Endpoints
All endpoints are prefixed with `/todo`.

- **Create a New Task**: `POST /todo/newTask`
- **Retrieve a Task**: `GET /todo/task`
- **List All Tasks**: `GET /todo/allTasks`
- **Delete a Task**: `DELETE /todo/removeTask`
- **Update a Task**: `PATCH /todo/updateTask/{id}`

## Project Structure
```
.
├── .idea/                   # Project settings
├── src/main/               # Source code
├── .gitignore              # Git ignore file
├── Dockerfile              # Dockerfile for app container
├── README.md               # Project documentation (this file)
├── config.yml              # Dropwizard configuration file
├── dependency-reduced-pom.xml
├── docker-compose.yaml     # Docker Compose configuration
├── init.sql                # SQL initialization script for MySQL
├── pom.xml                 # Maven project configuration
```

## Prerequisites
Make sure you have the following installed:
- Docker
- Docker Compose
- Java 11 or higher
- Maven

## Setup Instructions

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/jogendr2018s/todo-tracker-dropwizard.git
   cd todo-tracker-dropwizard
   ```

2. **Build the Application**:
   ```bash
   mvn clean package
   ```

3. **Run the Application**:
   Use Docker Compose to build and start the application:
   ```bash
   docker-compose up --build
   ```

4. **Access the Application**:
   - The application will be accessible at `http://localhost:8080`.
   - Use tools like Postman or curl to interact with the REST API endpoints.

5. **Database Setup**:
   - The `init.sql` file initializes the MySQL database with the necessary schema and tables.
   - MySQL will be running at `localhost:3306` (default credentials can be configured in `docker-compose.yaml`).

## Configuration
The application uses a `config.yml` file to manage configurations:
```yaml
server:
  applicationConnectors:
    - type: http
      port: 8080
  adminConnectors:
    - type: http
      port: 8081

database:
  driverClass: com.mysql.cj.jdbc.Driver
  user: root
  password: password
  url: jdbc:mysql://localhost:3306/todo_db
```

## Docker Compose
The `docker-compose.yaml` file defines the services:
```yaml
version: '3.8'
services:
  app:
    build: .
    ports:
      - "8080:8080"
    depends_on:
      - db
  db:
    image: mysql:8.0
    environment:
      MYSQL_ROOT_PASSWORD: password
      MYSQL_DATABASE: todo_db
    ports:
      - "3306:3306"
    volumes:
      - db_data:/var/lib/mysql
volumes:
  db_data:
```


