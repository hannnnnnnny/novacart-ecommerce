# NovaCart Fashion Commerce Product Requirements

## Product Vision

NovaCart is a fashion and lifestyle commerce platform with a polished customer storefront and a serious merchant operations workspace. It is not a grocery, food, or generic ecommerce demo.

The platform sells original sample catalog items across clothing, bags, jewelry, accessories, shoes, sportswear, sports equipment, seasonal collections, sale edits, and lifestyle fashion products.

## Customer Storefront Requirements

- Fashion homepage with hero campaign, category navigation, featured collections, seasonal campaign links, featured products, sale storytelling, and mobile-friendly layout.
- Product listing with category, collection, size, color, material, fictional label, season, sale, price, availability, sort, pagination, filter chips, loading states, error states, and empty states.
- Product detail with gallery, title, description, effective price, compare-at price, discount badge, size selector, color selector, quantity selector, stock status, material, care, shipping/return summary, add to cart, buy now, related products, collection link, and customer-care link.
- Cart with editable quantities, remove action, discount display, subtotal, shipping/tax preview through checkout, empty state, and continue shopping path.
- Demo checkout with customer name, email, phone, address, city, region, postal code, country, delivery method, demo payment method, refund acknowledgement, validation, stock check, and loading state.
- Order success page with order number, payment status, refund status, fulfillment status, shipping details, item summary, totals, refund window, support link, and refund request link.
- Support ticket flow for refund, exchange, shipping, product, payment, and other customer issues.
- Refund request flow with configurable refund window and admin review.

## Merchant Admin Requirements

- Dashboard with revenue periods, total orders, average order value, pending orders, refund requests, low-stock products, best sellers, sales trend, top regions, and customer preferences.
- Product management with create, edit, archive, reactivate, price, compare-at price, stock, size/color options, category assignment, collection assignment, imagery, tags, featured flag, preview link, search, filters, bulk discount, bulk archive, and bulk collection assignment.
- Collection management with create, edit, imagery, featured flag, status, sort order, date window, and product counts.
- Category management with create, edit, image, description, active flag, sort order, and safe delete behavior.
- Promotion management for percentage and fixed-amount discounts targeted to selected products, category, collection, season, or tags.
- Order management with customer details, payment status, fulfillment status, refund status, totals, line items, shipping data, search, status filters, and valid status transitions.
- Refund and support management with status updates and internal notes payload support.
- Inventory management with warning threshold controls, product-level low-stock thresholds, manual stock adjustments, stock movement history, and replenishment review.
- Customer records for guest checkout profiles based on email.
- Analytics for sales periods, sales trend, customer regions, category/size/color preferences, best sellers, repeat customers, and average order value.

## Data Requirements

- At least 60 original fashion products.
- At least 10 fashion categories.
- At least 6 original seasonal collections: Spring Edit, Summer Essentials, Workwear Capsule, Evening Details, Active Weekend, and End of Season Sale.
- No copyrighted brand names, logos, product photos, or copied product descriptions.
- Local sample imagery is repository-owned SVG artwork under `frontend/public/catalog`.

## Non-Goals

- Real payment processing.
- Customer account passwords and saved addresses.
- Real carrier rating or label purchase.
- Production audit logging and fraud controls.

## Future Roadmap

- Customer accounts and authenticated order history.
- Real payment provider integration behind explicit configuration.
- Admin audit trail.
- Reserved stock and supplier notes.
- Full E2E test suite in CI.
- Production observability, backups, rate limiting, and deployment hardening.
