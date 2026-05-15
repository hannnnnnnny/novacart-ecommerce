# NovaCart Backend

The backend module contains the Spring Boot REST API for NovaCart Ecommerce. It supports the public storefront, protected merchant administration, JWT authentication, MySQL persistence, checkout stock validation, order management, inventory warnings, seed data, and consistent JSON responses.

## Current Implementation

- Java 21 and Spring Boot 4.0.6.
- Maven wrapper for repeatable builds.
- Spring Web MVC REST controllers.
- Spring Data JPA and Hibernate entities for admins, categories, products, orders, and order items.
- MySQL runtime persistence with H2 test profile support.
- JWT authentication for protected admin APIs.
- BCrypt password hashing for admin accounts.
- Seed data for the default local admin, storefront categories, and sample products.
- Transactional checkout logic with product row locking to protect stock counts.
- Global exception handling with consistent English JSON error responses.
- Backend tests for application startup and checkout stock behavior.

## API Areas

Public storefront endpoints:

- `GET /api/public/categories`
- `GET /api/public/products`
- `GET /api/public/products/{id}`
- `POST /api/public/orders`
- `GET /api/public/orders/{id}`

Admin authentication:

- `POST /api/admin/auth/login`

Protected admin endpoints:

- `GET /api/admin/dashboard/metrics`
- `GET /api/admin/inventory/warnings`
- `GET /api/admin/categories`
- `POST /api/admin/categories`
- `PUT /api/admin/categories/{id}`
- `DELETE /api/admin/categories/{id}`
- `GET /api/admin/products`
- `GET /api/admin/products/{id}`
- `POST /api/admin/products`
- `PUT /api/admin/products/{id}`
- `DELETE /api/admin/products/{id}`
- `GET /api/admin/orders`
- `GET /api/admin/orders/{id}`
- `PATCH /api/admin/orders/{id}/status`

## Local Requirements

- Java 21
- MySQL 8 or compatible database

## Environment Variables

See [`.env.example`](.env.example) for the full local configuration template.

Required production values should be supplied through environment variables or a secret manager. Do not commit real credentials.

## Commands

From the `backend/` directory:

```bash
./mvnw test
./mvnw spring-boot:run
```

On Windows PowerShell:

```powershell
.\mvnw.cmd test
.\mvnw.cmd spring-boot:run
```

## Test Profile

Tests use `application-test.yml`, H2 in MySQL compatibility mode, and the `test` Spring profile. This keeps backend checks independent from a local MySQL server.

## Security Notes

- The seeded admin account is for local development only.
- Replace `JWT_SECRET` with a long, random production secret.
- Keep database credentials, JWT secrets, and deployment settings out of version control.
- This backend does not process real payments. Checkout creates orders and adjusts stock only.
