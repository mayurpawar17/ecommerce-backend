# 🛒 Ecommerce Backend

A production-ready **RESTful ecommerce backend** built with **Spring Boot 4**, featuring JWT-based authentication, role-based access control, and a clean feature-based architecture.

---

## 🚀 Tech Stack

| Layer | Technology |
|---|---|
| Framework | Spring Boot 4.0.5 |
| Language | Java 17 |
| Security | Spring Security + JWT (jjwt 0.12.6) |
| Database | PostgreSQL |
| ORM | Spring Data JPA / Hibernate |
| Validation | Jakarta Bean Validation (JSR-380) |
| Build Tool | Maven |
| Utilities | Lombok |

---

## ✨ Features

- 🔐 **JWT Authentication** — Stateless access tokens + refresh token rotation
- 👥 **Role-Based Access Control** — `ROLE_ADMIN` and `ROLE_USER` roles
- 📦 **Product Management** — Full CRUD with pagination and sorting (Admin only for writes)
- 🗂️ **Category Management** — Hierarchical product categorization (Admin only for writes)
- 🛍️ **Cart** — Add items to cart per user
- 🧾 **Orders** — Checkout flow converting cart to order
- 🧑 **User Profiles** — Retrieve authenticated user info
- 🛡️ **Global Exception Handling** — Consistent `ApiResponse` wrapper for all endpoints
- ✅ **Input Validation** — Request body validation with descriptive error messages

---

## 📂 Project Structure

```
src/main/java/dev/mayur/ecommerce_backend/
├── EcommerceBackendApplication.java
├── core/
│   ├── exception/
│   │   ├── GlobalExceptionHandler.java
│   │   └── custom/
│   │       └── ResourceNotFoundException.java
│   └── utils/
│       ├── config/
│       │   ├── AppConfig.java
│       │   └── SecurityConfig.java
│       ├── dto/
│       │   ├── ApiResponse.java
│       │   └── Pagination.java
│       ├── enums/
│       │   └── Role.java
│       └── security/
│           ├── CustomAccessDeniedHandler.java
│           ├── CustomAuthenticationEntryPoint.java
│           └── SecurityUtils.java
└── features/
    ├── auth/
    │   ├── controller/AuthController.java
    │   ├── dto/                          # LoginRequest, RegisterRequest, AuthResponse, ...
    │   ├── entity/                       # User, RefreshToken
    │   ├── jwt/                          # JwtUtil, JwtAuthFilter
    │   ├── repo/
    │   └── service/                      # AuthService, RefreshTokenService, CurrentUserService
    ├── product/
    │   ├── controller/ProductController.java
    │   ├── dto/
    │   ├── entity/Product.java
    │   ├── repo/
    │   └── service/
    ├── category/
    │   ├── controller/CategoryController.java
    │   ├── dto/
    │   ├── entity/
    │   ├── repo/
    │   └── service/
    ├── cart/
    │   ├── controller/CartController.java
    │   ├── dto/
    │   ├── entity/
    │   ├── repo/
    │   └── service/
    ├── order/
    │   ├── controller/OrderController.java
    │   ├── dto/
    │   ├── entity/                       # Order, OrderItem
    │   ├── repo/
    │   └── service/
    └── user/
        └── ...
```

---

## 🌐 API Endpoints

All endpoints are prefixed with `/api/v1`.

### 🔐 Auth — `/api/v1/auth`

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| `POST` | `/register` | Public | Register a new user |
| `POST` | `/login` | Public | Login and receive access + refresh tokens |
| `GET` | `/profile` | 🔒 Any | Get current user's profile |
| `POST` | `/refresh` | Public | Get new access token via refresh token |
| `POST` | `/logout` | 🔒 Any | Logout and invalidate refresh token |

### 📦 Products — `/api/v1/products`

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| `POST` | `/` | 🔒 Admin | Create a new product |
| `GET` | `/{id}` | 🔒 User/Admin | Get product by ID |
| `GET` | `/?page=0&size=10&sortBy=id&sortDir=asc` | 🔒 User/Admin | Get all products (paginated) |

### 🗂️ Categories — `/api/v1/categories`

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| `POST` | `/` | 🔒 Admin | Create a category |
| `GET` | `/{id}` | 🔒 User/Admin | Get category by ID |
| `GET` | `/?page=0&size=10&sortBy=id&sortDir=asc` | 🔒 User/Admin | Get all categories (paginated) |
| `PUT` | `/{id}` | 🔒 Admin | Update a category |
| `DELETE` | `/{id}` | 🔒 Admin | Delete a category |

### 🛍️ Cart — `/api/v1/cart`

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| `POST` | `/add/items?userId={id}` | 🔒 User | Add a product to cart |

### 🧾 Orders — `/api/v1/orders`

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| `POST` | `/checkout?userId={id}` | 🔒 User | Checkout and create an order from cart |

---

## ⚙️ Configuration

### Environment Variables

Create a `.env` file or set the following environment variables before running:

```env
DB_URL=jdbc:postgresql://localhost:5432/ecommerce
DB_USER=your_postgres_username
DB_PASSWORD=your_postgres_password
JWT_SECRET=your_very_long_and_secure_jwt_secret_key
```

### `application.yaml`

```yaml
spring:
  datasource:
    url: ${DB_URL}
    username: ${DB_USER}
    password: ${DB_PASSWORD}
    driver-class-name: org.postgresql.Driver

  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
```

> **Note:** `ddl-auto: update` will auto-create/update tables on startup. Switch to `validate` or `none` in production.

---

## 🏁 Getting Started

### Prerequisites

- Java 17+
- Maven 3.8+
- PostgreSQL (running locally or remote)

### Run Locally

```bash
# Clone the repository
git clone https://github.com/mayurpawar17/ecommerce-backend.git
cd ecommerce-backend

# Set environment variables (or export them in your shell)
export DB_URL=jdbc:postgresql://localhost:5432/ecommerce
export DB_USER=postgres
export DB_PASSWORD=your_password

# Run with Maven wrapper
./mvnw spring-boot:run
```

The server starts on **`http://localhost:8080`** by default.

### Build JAR

```bash
./mvnw clean package -DskipTests
java -jar target/ecommerce-backend-0.0.1-SNAPSHOT.jar
```

---

## 🔒 Authentication Flow

```
1. POST /api/v1/auth/register  →  Create account
2. POST /api/v1/auth/login     →  Receive { accessToken, refreshToken }
3. Use accessToken in header:  Authorization: Bearer <accessToken>
4. POST /api/v1/auth/refresh   →  Exchange refreshToken for new accessToken
5. POST /api/v1/auth/logout    →  Invalidate refreshToken
```

---

## 🗃️ Data Model Overview

```
User          → id, name, email, password, role, enabled
RefreshToken  → id, token, user (FK), expiryDate
Category      → id, name, description
Product       → id, name, description, price, stockQuantity, category (FK)
Cart          → id, userId, items [CartItem]
CartItem      → id, product (FK), quantity
Order         → id, userId, totalAmount, status, items [OrderItem]
OrderItem     → id, product (FK), quantity, price
```

---

## 🛡️ Security Model

- All endpoints except `/api/v1/auth/register` and `/api/v1/auth/login` require a valid JWT.
- `ROLE_ADMIN` can perform all write operations (create/update/delete products, categories).
- `ROLE_USER` can browse products/categories, manage their own cart, and place orders.
- Custom `AccessDeniedHandler` and `AuthenticationEntryPoint` return structured JSON errors instead of HTML.

---

## 📋 Response Format

All endpoints return a consistent `ApiResponse` envelope:

```json
{
  "success": true,
  "message": "Products fetched successfully!",
  "data": [...],
  "pagination": {
    "page": 0,
    "size": 10,
    "totalElements": 42,
    "totalPages": 5,
    "last": false
  }
}
```

---

## 🧑‍💻 Author

**Mayur Pawar** — [@mayurpawar17](https://github.com/mayurpawar17)

---

## 📄 License

This project is open-source and available under the [MIT License](LICENSE).
