# NovaCart Multi-Merchant Ecommerce Website Builder Requirements

## Product Vision

NovaCart is a SaaS-style ecommerce website builder where many merchants can create, customize, manage, and publish their own online stores. The product includes a public platform website, merchant onboarding flow, template selector, generated storefronts, and a protected merchant operations workspace.

NovaCart is not one store. It is the platform merchants use to launch stores for fashion, thrift, sports, home goods, lifestyle, and other retail categories.

## Platform Requirements

- Public platform landing page explaining NovaCart as a store builder.
- Feature, template, and pricing pages for merchant evaluation.
- Merchant signup/login entry points.
- Guided onboarding for store basics, template choice, first products, brand configuration, and storefront preview.
- Store slug routing under `/store/:storeSlug` for local multi-store support without subdomains.
- Demo stores for `demo-fashion`, `demo-sports`, `demo-home`, and `demo-boutique`.

## Generated Customer Storefront Requirements

- Storefront homepage displays merchant name/logo, template styling, announcement bar, hero, categories, and featured products.
- Product listing is scoped by store slug and uses store-specific products and categories.
- Product detail, cart, checkout, order success, and support/refund paths stay under `/store/:storeSlug`.
- Checkout remains demo-safe and local for frontend-generated stores until backend multi-store checkout is fully wired.
- Storefront styling changes based on selected template and merchant brand settings.
- Support ticket flow for refund, exchange, shipping, product, payment, and other customer issues.
- Refund request flow with configurable refund window and admin review.

## Merchant Admin Requirements

- Dashboard with revenue periods, total orders, average order value, pending orders, refund requests, low-stock products, best sellers, sales trend, top regions, and customer preferences.
- Store setup checklist for details, template, products, shipping, preview, and publish state.
- Store switcher for current merchant store context.
- Template selector and theme editor for logo text, brand color, font style, hero copy, announcement text, and button style.
- Product management with create, edit, archive, reactivate, price, compare-at price, stock, size/color options, category assignment, collection assignment, imagery, tags, featured flag, preview link, search, filters, bulk discount, bulk archive, and bulk collection assignment.
- Collection management with create, edit, imagery, featured flag, status, sort order, date window, and product counts.
- Category management with create, edit, image, description, active flag, sort order, and safe delete behavior.
- Promotion management for percentage and fixed-amount discounts targeted to selected products, category, collection, season, or tags with affected-product preview.
- Order management with customer details, payment status, fulfillment status, refund status, totals, line items, shipping data, search, status filters, and valid status transitions.
- Refund and support management with status updates and internal notes payload support.
- Inventory management with warning threshold controls, product-level low-stock thresholds, manual stock adjustments, stock movement history, and replenishment review.
- Customer records for guest checkout profiles based on email.
- Analytics for sales periods, sales trend, customer regions, category/size/color preferences, best sellers, repeat customers, and average order value.

## Data Requirements

- Merchant account concept.
- Store/shop concept with slug, category, description, template, brand settings, currency, shipping message, announcement, and publish state.
- Product catalog, orders, customers, inventory, promotions, analytics, support, and refunds should ultimately be scoped by store.
- At least 60 original fashion products.
- At least 10 fashion categories.
- At least 6 original seasonal collections: Spring Edit, Summer Essentials, Workwear Capsule, Evening Details, Active Weekend, and End of Season Sale.
- No copyrighted brand names, logos, product photos, or copied product descriptions.
- Local sample imagery is repository-owned generated demo artwork. Generated merchant storefronts use realistic JPEG assets under `frontend/public/demo-images`; legacy SVG catalog assets remain available for backend seed records and older catalog references.

## Non-Goals

- Real payment processing.
- Real subdomain provisioning in local development.
- Full backend migration of every existing catalog/order table to store-scoped queries in the first platform repositioning pass.
- Customer account passwords and saved addresses.
- Real carrier rating or label purchase.
- Production audit logging and fraud controls.

## Future Roadmap

- API-backed onboarding and persistent store creation.
- Store-scoped product, order, customer, promotion, inventory, support, refund, and analytics queries.
- Custom domains and subdomain routing.
- Customer accounts and authenticated order history.
- Real payment provider integration behind explicit configuration.
- Admin audit trail.
- Reserved stock and supplier notes.
- Full E2E test suite in CI.
- Production observability, backups, rate limiting, and deployment hardening.
