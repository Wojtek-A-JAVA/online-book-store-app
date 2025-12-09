# 📚 Online Book Store API

[![Java](https://img.shields.io/badge/Java-17%2B-orange?style=for-the-badge&logo=java)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen?style=for-the-badge&logo=spring-boot)](https://spring.io/projects/spring-boot)
[![Docker](https://img.shields.io/badge/Docker-Enabled-blue?style=for-the-badge&logo=docker)](https://www.docker.com/)
[![MySQL](https://img.shields.io/badge/MySQL-DB-4479A1?style=for-the-badge&logo=mysql)](https://www.mysql.com/)

My first RESTful API for a web bookstore application. This project provides a backend solution that allows users to securely process orders, manage shopping carts, browse books, and place orders.

---

## 📑 Table of Contents
- [✨ Key Features](#-key-features)
- [🛠️ Technologies Used](#-technologies-used)
- [⚙️ Architecture & Design](#-architecture--design)
- [🚀 Getting Started](#-getting-started)
    - [Prerequisites](#prerequisites)
    - [Installation](#installation)
    - [Environment Configuration](#environment-configuration)
    - [Running with Docker](#running-with-docker)
- [🔌 API Endpoints](#-api-endpoints)
- [🗄️ Database Management](#-database-management)
- [🎥 Project presentation](#-project-presentation)

---

## ✨ Key Features

*   **🔐 Authentication & Authorization:** Secure user registration and login using JWT (JSON Web Tokens). Role-based access control (Admin/User).
*   **📚 Book Management:** CRUD operations for books with support for pagination and sorting.
*   **🗂️ Categories:** Organize books into categories for easier navigation.
*   **🛒 Shopping Cart:** Persistent shopping cart functionality; add, remove, and update items.
*   **📦 Order Processing:** comprehensive order lifecycle management.
*   **🔍 Search:** Advanced search capabilities for finding books.

---

## 🛠️ Technologies Used

### Core Frameworks & Languages
*   **Java 17+**: The fundamental programming language.
*   **Spring Boot 3**: For rapid application development and dependency injection.
*   **Spring Security**: For robust authentication and authorization.
*   **Spring Data JPA**: For efficient database interaction and repository abstraction.

### Database & Infrastructure
*   **MySQL**: Relational database management system.
*   **Docker & Docker Compose**: Containerization for the application and database.
*   **Liquibase**: (If applicable) Database schema migration and version control.

### Tools & Libraries
*   **MapStruct**: For efficient DTO-to-Entity mapping.
*   **Lombok**: To reduce boilerplate code.
*   **Maven**: Dependency management and build tool.
*   **Swagger/OpenAPI**: (Optional/Recommended) API documentation.
*   **Checkstyle**: Enforcing coding standards.

---

## ⚙️ Architecture & Design

The application follows a standard **Layered Architecture**:

1.  **Controller Layer**: Handles incoming HTTP requests (`/controller`).
2.  **Service Layer**: Contains business logic (`/service`).
3.  **Repository Layer**: Interacts with the database (`/repository`).
4.  **Model/Domain Layer**: Represents database entities (`/model`).
5.  **DTOs**: Data Transfer Objects for client-server communication (`/dto`).
6.  **Mappers**: Handles object transformation (`/mapper`).

---

## 🚀 Getting Started

### Prerequisites
Ensure you have the following installed:
*   [Java SDK 17+](https://www.oracle.com/java/technologies/downloads/)
*   [Maven](https://maven.apache.org/download.cgi)
*   [Docker Desktop](https://www.docker.com/products/docker-desktop/)

### Installation

1.  **Clone the repository**
    ```bash
    git clone https://github.com/your-username/online-book-store-app.git
    cd online-book-store-app
    ```

2.  **Build the project**
    ```bash
    mvn clean package -DskipTests
    ```

### Environment Configuration

This project uses environment variables for sensitive configuration.

1.  Find the `.env.template` file in the root directory.
2.  Create a copy named `.env`.
3.  Fill in your specific configuration details (User, Password, JWT Secret, etc.).

### Running with Docker

Build & start: docker compose up --build (If you keep a single-stage Dockerfile that needs a JAR: run mvn -DskipTests package first.) 

Swagger/UI: http://localhost:${SPRING_LOCAL_PORT}/api/swagger-bookstore.html

---

## 🔌 API Endpoints

Here is a summary of the available resources.

| Feature | HTTP Verbs | Endpoint                               | Description                    |
| :--- |:-----------|:---------------------------------------|:-------------------------------|
| **Auth** | `POST`     | `/api/auth/registration`      | Register a new user            |
| | `POST`     | `/api/auth/login`                      | Login and receive JWT          |
| **Books** | `GET`      | `/api/books`                 | Get all books (pageable)       |
| | `GET`      | `/api/books/{id}`                      | Get book details               |
| | `GET`      | `/api/books/search`                    | Search for a book (pageable)   |
| | `POST`     | `/api/books`                           | Create a book (Admin)          |
| | `PUT`      | `/api/books/{id}`                      | Update book data (Admin)       |
| | `DELETE`   | `/api/books/{id}`                      | Delete a book (Admin)          |
| | `DELETE`   | `/api/books`                           | Delete all books (Admin)       |
| **Categories** | `GET`      | `/api/categories`       | List all categories            |
| | `GET`      | `/api/categories/{id}`                 | Get category                   |
| | `GET`      | `/api/categories/{id}/books`           | Get books by category          |
| | `POST`     | `/api/categories`                      | Create category (Admin)        |
| | `PUT`      | `/api/categories/{id}`                 | Update category (Admin)        |
| | `DELETE`   | `/api/categories/{id}`                 | Delete category (Admin)        |
| **Shopping Cart** | `GET`      | `/api/cart`          | View user's cart               |
| | `POST`     | `/api/cart`                            | Add item to cart               |
| | `PUT`      | `/api/cart-items/{id}`                 | Update item quantity           |
| | `DELETE`   | `/api/cart-items/{id}`                 | Delete item from shopping cart |
| **Orders** | `GET`      | `/api/orders`               | Get all orders                 |
| | `GET`      | `/api/orders/{id}/items`               | Get all order items            |
| | `GET`      | `/api/orders/{orderId}/items/{itemId}` | Get order item from order      |
| | `POST`     | `/api/orders`                          | Create order                   |
| | `PATCH`    | `/api/orders/{id}`                     | Update order status (Admin)    |

---

## 🗄️ Database Management

### Manually Assigning Roles
You need to grant privileges to users manually. You can execute the following SQL command in your database client or Docker container:

1.  **Access the Database** :
    ```bash
    docker exec -it <container_name> mysql -u <username> -p
    ```
2.  **Run the Query**:
    ```sql
    USE book_store; -- or your DB name

    -- Find the user ID first
    SELECT id, email FROM users WHERE email = 'admin@example.com';

    -- Insert the role mapping (assuming role_id 1 is ADMIN)
    INSERT INTO users_roles (user_id, role_id) VALUES (<user_id>, 1);
    ```

---

## 🗄️ Project presentation


---
