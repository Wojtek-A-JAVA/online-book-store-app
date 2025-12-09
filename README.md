# Spring Boot Portfolio Project
To access Swagger UI on localhost: localhost:${SPRING_LOCAL_PORT}/api/swagger-bookstore.html

# Run with Docker
Copy .env.template to .env and adjust values.
Build & start: docker compose up --build
(If you keep a single-stage Dockerfile that needs a JAR: run mvn -DskipTests package first.)
Swagger/UI: http://localhost:${SPRING_LOCAL_PORT}/api/swagger-bookstore.html
