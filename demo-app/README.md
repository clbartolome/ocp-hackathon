# Demo App

Spring Boot REST CRUD application for managing bank accounts backed by PostgreSQL.

## Endpoints

| Method | URI            | Description                  |
|--------|----------------|------------------------------|
| GET    | /              | INDEX html page              |
| GET    | /accounts      | List all bank accounts       |
| GET    | /accounts/{id} | Get a single account by ID   |
| POST   | /accounts      | Create a new account         |
| PUT    | /accounts/{id} | Update an existing account   |
| DELETE | /accounts/{id} | Delete an account            |

## Configuration

The application reads database connection details from environment variables:

- `DB_HOST` (default: `localhost`)
- `DB_PORT` (default: `5432`)
- `DB_NAME` (default: `demo`)
- `DB_USER` (default: `user`)
- `DB_PASSWORD` (default: `password`)

## Running Locally

0. **Run tests**:
    ```sh
    mvn test
    ```

1. **Create local database** (example):
    ```sh
    podman run -d --name demo-db -e POSTGRESQL_USER=user -e POSTGRESQL_PASSWORD=password -e POSTGRESQL_DATABASE=demo -p 5432:5432 registry.redhat.io/rhel9/postgresql-16
    ```
    
2. **Build, Test & Run**:
    ```sh
    mvn clean package
    mvn spring-boot:run
    ```

