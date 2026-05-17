# NovaCart Architecture

NovaCart is organized as a full-stack fashion commerce application with a public storefront, a protected merchant administration workspace, and a Spring Boot API that owns validation, persistence, authentication, and checkout rules.

## System Overview

```mermaid
flowchart LR
  Browser["Vue 3 Frontend"] --> Api["Spring Boot REST API"]
  Api --> Database["MySQL"]
  Api --> Security["JWT And BCrypt"]
  Browser --> LocalStorage["Cart And Admin Session Storage"]
```

## Backend Layers

- `controller`: REST endpoint boundaries for public catalog, checkout, admin auth, catalog management, order management, dashboard metrics, inventory warnings, and manual stock adjustments.
- `dto`: Request and response contracts for API payloads, validation errors, login responses, dashboard metrics, and order data.
- `service`: Business interfaces for catalog, checkout, authentication, and dashboard behavior.
- `service/impl`: Transactional business logic, stock deduction, slug generation, authentication, and DTO mapping.
- `repository`: Spring Data JPA repositories for admins, categories, products, orders, product locking, and stock movement history.
- `entity`: JPA models for admins, categories, products, customer orders, order items, order statuses, payment statuses, shipping methods, and stock movements.
- `security`: JWT generation/parsing, bearer-token request filtering, and admin user loading.
- `exception`: Domain exceptions and global JSON error handling.
- `config`: Security, CORS, password encoding, and seed data configuration.

## Frontend Structure

- `api`: Axios client wrapper and endpoint modules for catalog, orders, and admin APIs.
- `assets`: Global responsive CSS and design system primitives.
- `components`: Reusable UI building blocks such as headers, cards, status badges, loading states, empty states, errors, metrics, and quantity controls.
- `layouts`: Storefront and admin shells.
- `pages`: Route-level storefront and admin workflows.
- `router`: Vue Router configuration and admin route guards.
- `stores`: Pinia stores for cart and admin session state.
- `utils`: Formatting helpers for currency, dates, and statuses.

## Authentication Flow

1. An admin submits email and password to `POST /api/admin/auth/login`.
2. The backend verifies the active admin user and BCrypt password hash.
3. The backend returns a JWT bearer token with email subject, role claim, and expiration.
4. The frontend stores the session in local storage and sends `Authorization: Bearer <token>` on admin API requests.
5. The JWT filter validates bearer tokens before protected endpoints.
6. Missing, expired, or invalid tokens return a consistent `401` JSON error. Authenticated users without access receive `403`.

## Checkout Flow

1. The storefront cart stores product snapshots and quantities in browser local storage.
2. Checkout submits customer information, shipping details, demo payment selection, idempotency key, and product quantities to `POST /api/public/orders`.
3. The order service aggregates duplicate product IDs into a single requested quantity per product.
4. Each product is loaded with a pessimistic database lock.
5. The backend rejects inactive products, missing products, invalid quantities, insufficient stock, and empty carts.
6. The demo payment step can approve or safely decline without collecting real payment details.
7. Valid checkout deducts stock, records stock movements, creates order items, calculates subtotal, shipping, tax, discount, and total amounts, and saves the customer order in one transaction.
8. Repeated requests with the same idempotency key return the original order instead of deducting stock twice.
9. The frontend clears the cart and routes to the order success page.

## Database Entities

- `AdminUser`: Admin email, BCrypt password hash, role, active flag, and timestamps.
- `Category`: Catalog grouping with name, slug, optional description, active flag, and timestamps.
- `Product`: Product catalog data, SKU, brand, price, compare-at price, stock quantity, low-stock threshold, image gallery, tags, featured flag, product status, active flag, category relationship, and timestamps.
- `CustomerOrder`: Customer contact/shipping data, order number, idempotency key, payment status, shipping method, fulfillment status, totals breakdown, order items, and timestamps.
- `OrderItem`: Product snapshot, unit price, quantity, and line total for each order.
- `StockMovement`: Inventory event history for checkout deductions, cancellation restorations, manual adjustments, and seed data.

## Fashion Catalog Model

The fashion release extends the catalog around retail merchandising rather than generic ecommerce inventory:

- `Product`: name, slug, description, category, collection, price, compare-at price, computed effective promotion price, SKU, stock quantity, low-stock threshold, status, size options, color options, material, care instructions, primary image, gallery, tags, season, gender target, featured flag, and timestamps.
- `FashionCollection`: name, slug, description, hero image, display image, active/draft/archive status, featured flag, date window, sort order, and product assignment through product records.
- `Category`: name, slug, description, display image, sort order, and active flag.
- `Promotion`: name, description, discount type, discount value, date window, active flag, target type, target values, and timestamps.
- `CustomerProfile`: guest customer record keyed by checkout email with contact, address summary, country, region, city, creation date, and last order date.
- `SupportTicket` and `RefundRequest`: customer care records linked to order number/email and managed from protected admin screens.

All sample data is original to this repository. Local storefront imagery uses generated SVG assets under `frontend/public/catalog`.

## Promotion Flow

1. Admin creates a promotion for selected products, a category, a collection, a season, or one or more tags.
2. The backend validates discount type, positive discount value, fixed discount price floor, and date range.
3. Public catalog responses quote each product through the promotion service.
4. Product cards, detail pages, cart, and checkout display the effective price, original price, and discount badge.
5. Checkout recalculates product prices server-side so local cart snapshots cannot force a stale or invalid total.

## Refund And Support Flow

1. A customer submits a support ticket or refund request from storefront customer care pages.
2. Refund requests verify order number, email, payment/refund status, and the refund window.
3. Admin users can move support tickets through `OPEN`, `IN_REVIEW`, `WAITING_FOR_CUSTOMER`, `RESOLVED`, and `CLOSED`.
4. Admin users can move refunds through `REQUESTED`, `UNDER_REVIEW`, `APPROVED`, `REJECTED`, and `REFUNDED`.
5. Approved/refunded transitions update the order refund status and payment status consistently.

## Inventory Adjustment Flow

1. Admin users review low-stock warnings from `/api/admin/inventory/warnings`.
2. A manual stock adjustment posts `productId`, signed `quantityChange`, and a merchant reason to `/api/admin/inventory/adjustments`.
3. The backend locks the product row, rejects zero changes, prevents negative stock, updates the stock quantity, and records a `MANUAL_ADJUSTMENT` stock movement.
4. The admin inventory page refreshes warnings and movement history so the merchant can confirm the adjustment immediately.

## Analytics Flow

1. Checkout upserts a guest `CustomerProfile` by email and links the order to that profile.
2. Admin analytics aggregates non-cancelled orders for daily, weekly, monthly, yearly, and total revenue.
3. Region and country metrics come from checkout shipping fields.
4. Customer preference metrics are derived from purchased categories, selected colors, and selected sizes.
5. Best-seller metrics aggregate order items by product snapshot.

## API Response Shape

Successful responses use:

```json
{
  "success": true,
  "message": "Products loaded successfully.",
  "data": [],
  "timestamp": "2026-05-15T00:00:00Z"
}
```

Error responses use:

```json
{
  "success": false,
  "message": "Validation failed. Please review the highlighted fields.",
  "status": 400,
  "path": "/api/public/orders",
  "errors": [
    {
      "field": "customerEmail",
      "message": "Enter a valid email address."
    }
  ],
  "timestamp": "2026-05-15T00:00:00Z"
}
```
