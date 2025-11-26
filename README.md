# Expense Tracker API

A Spring Boot application for managing personal finances, tracking expenses, and budget planning with real-time notifications and automated reporting.

## Features
- User authentication and authorization (JWT-based)
- Role-based access control (User/Admin)
- Expense tracking and categorization
- Budget planning and monitoring
- Real-time notifications via WebSocket
- Automated expense reports generation (Excel/PDF)
- Budget exceeded alerts
- Scheduled expense reporting
- RESTful API with Swagger documentation
- Caching support (Caffeine)
# Tech Stack
- **Backend:** Spring Boot 3.5.7, Java 21
- **Security:** Spring Security with JWT
- **Database:** PostgreSQL
- **Migration:** Flyway
- **Build Tool:** Gradle
- **Documentation:** Swagger/OpenAPI
- **Real-time Communication:** WebSocket (STOMP)
- **Validation:** Jakarta Bean Validation
- **Caching:** Caffeine Cache
- **Logging:** SLF4J
- **Containerization:** Docker & Docker Compose

## Installation

1) Clone the repository:
```bash
git clone https://github.com/BortnikD/Expense-tracker
```
2)  Move to directory
   ```bash
cd ExpenseTracker
   ```
3) Set up your .env file
```bash
cp .env.example .env
```
**Сhange the variables to your own!!!**  
4) Run build
```bash
docker compose up --build
```


## Project Structure
```
src/main/java/com/bortnik/expensetracker/
├── config/              - Application configuration
│   ├── AdminInitializer.java
│   ├── CorsConfig.java
│   ├── JacksonConfig.java
│   ├── SwaggerConfig.java
│   └── WebSocketConfig.java
├── controller/          - REST controllers
│   ├── admin/          - Admin-specific endpoints
│   ├── user/           - User-specific endpoints
│   └── validator/      - Request validators
├── dto/                 - Data Transfer Objects
├── entities/            - JPA entities
├── exceptions/          - Custom exceptions
├── filters/             - Request/response filters
├── repository/          - JPA repositories
├── scheduler/           - Scheduled tasks
├── security/            - Security & JWT
│   ├── jwt/            - JWT provider & filter
│   └── service/        - UserDetailsService
├── service/             - Business logic
└── util/                - Utility classes

src/main/resources/
├── application.properties
├── db/migration/        - Flyway migrations
├── static/
└── templates/
```
## Authentication

**The application uses JWT-based authentication:**

1) Register a new user: POST `/api/auth/register`
```json
{
  "username": "string",
  "email": "user@example.com",
  "password": "stringst"
}
```

2) Login to get JWT token: POST `/api/auth/login`
```json
{
  "username": "string",
  "password": "stringst"
}
```

3) **Include token in subsequent requests:** `Authorization: Bearer <your-jwt-token>`
## Default Admin Account
On first run, an admin account is created:

Username: Configured via `ADMIN_USERNAME`
Password: Configured via `ADMIN_PASSWORD`
Role: `ADMIN`

## WebSocket Support

Real-time notifications via WebSocket endpoint: `ws://localhost:8080/ws`
Subscribe to user-specific notifications: `/user/{userId}/notifications`

*Example (JavaScript):*
```js
const socket = new SockJS('http://localhost:8080/ws');  
const stompClient = Stomp.over(socket);  
  
stompClient.connect({}, () => {  
  stompClient.subscribe(`/user/${userId}/notifications`, (message) => {  
    console.log('Notification:', JSON.parse(message.body));  
  });  
});
```

## Scheduled Tasks
###  Report Scheduler

- Cron: Configurable schedule
- Actions:
    - Generates monthly expense reports
    - Sends notifications about budget status
    - Alerts users when budgets are exceeded
- Configuration: `ExpenseReportScheduler.java`

## Error Handling

Global exception handling via `ExceptionsHandler`:
```json
{  
  "timestamp": "2025-11-21T18:59:26+03:00",  
  "success": false,  
  "data": null,  
  "apiError": {  
    "error": "Error Type",  
    "message": "Detailed error description",  
    "status": "HTTP_STATUS"  
  },  
  "metadata": null  
}
```

## Security
### Features:
- BCrypt password encoding (strength 10)
- JWT token-based authentication (30-day expiration)
- CORS configuration for frontend integration
- Role-based authorization (USER/ADMIN)
- Request logging via `RequestLoggingFilter`
- Secure password validation
