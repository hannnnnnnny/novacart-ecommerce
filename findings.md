# NovaCart Audit Findings

## Skill Findings
- `planning-with-files` is active for persistent task tracking through `task_plan.md`, `findings.md`, and `progress.md`.
- `ui-ux-pro-max` design-system output recommended a premium black/gold-neutral ecommerce SaaS direction, clean typography, restrained effects, visible focus states, and semantic Vue templates.
- `ui-ux-pro-max` Vue guidance emphasized semantic elements and dynamically bound ARIA state.
- `find-skills` search for `figma design system` found:
  - `figma/mcp-server-guide@figma-create-design-system-rules` with about 1.6K installs.
  - `figma/mcp-server-guide@create-design-system-rules` with about 1.3K installs.
  - Lower-install community Figma implementation skills.
- Figma-related skill not installed yet because this task has no Figma file, design-token export, or Figma MCP source to consume. Installing it now would add little practical value.

## Repository State
- Branch: `main`.
- Remote: `origin` points to `https://github.com/hannnnnnnny/NovaCart-Fashion-Commerce-Platform.git`.
- Local branch is currently aligned with `origin/main`.

## Audit Findings
- Technology stack:
  - Frontend: Vue 3, Vite, Pinia, Vue Router, Axios, lucide icons, Vitest.
  - Backend: Java 21, Spring Boot 4, Spring Security/JWT, JPA, MySQL runtime, H2 tests.
- Route structure now includes:
  - Platform pages under `/`, `/features`, `/templates`, `/pricing`, `/signup`, `/onboarding`.
  - Merchant generated storefront routes under `/store/:storeSlug`.
  - Protected merchant admin routes under `/admin/*`.
- Legacy single-store customer pages still exist under `frontend/src/pages` (`HomePage`, `ProductListPage`, `ProductDetailPage`, `CartPage`, `CheckoutPage`, `SupportPage`, `RefundRequestPage`, `OrderSuccessPage`) but public `/products`, `/cart`, and `/checkout` redirect to `/store/demo-fashion/...`.
- `frontend/index.html` still says `NovaCart Fashion Commerce Platform`; metadata has not been updated for the multi-merchant SaaS positioning and has no description/Open Graph/Twitter metadata.
- `backend/pom.xml` still describes the backend as `NovaCart Fashion Commerce Platform`; wording should be refreshed to multi-merchant ecommerce builder.
- `backend/package-lock.json` exists even though the backend is Maven/Spring Boot. It should be checked before removal because it may have been accidentally added by npm tooling.
- The current route map has no global catch-all/404 route. Unknown routes may render a blank or router default experience.
- Store/state audit:
  - `cart.js` has good normalization for the legacy backend-powered cart, including variant line keys and invalid localStorage cleanup.
  - `storefrontCart.js` is simpler and does not normalize loaded localStorage carts. Corrupt or stale store cart data can leak invalid quantities/prices into generated storefronts.
  - `storefrontCart.js` computes totals from local product snapshots only, which is acceptable for frontend-generated demo stores but must be clearly treated as demo-safe.
  - Platform-created stores are stored in localStorage; this is intentional frontend groundwork but not backend-persistent yet.
- API audit:
  - Axios wrapper clears expired admin sessions and maps validation errors.
  - Public/store API wrappers exist, but generated storefronts still primarily use frontend data rather than backend `MerchantStore` data.
  - Admin API wrappers cover products, categories, collections, promotions, orders, refunds, support, analytics, customers, and inventory.
- Interaction audit:
  - Many buttons have real handlers or disabled states.
  - Need inspect legacy pages/components because they still reference route names like `products`, `support`, and `refund-request` even though those named routes are absent from the current router.
  - Need inspect template preview action: `/store/:slug?templatePreview=...` may not actually switch the storefront template.
- Generated storefront audit:
  - Product cards now show option summaries and correctly route shoppers to product detail when size/color selection is required.
  - Product detail supports size/color selectors and disables add/buy controls until required selections are made.
  - Generated checkout lacks postal code and delivery method fields, has weak email validation, and has no submitting/loading state even though the button places an order.
  - Generated support/refund flow works for backend requests and local demo refunds, but does not initialize refund mode from a `mode=refund` query yet.
  - Wishlist/favorites are not implemented for generated storefronts.
- Routing audit:
  - Legacy `/products`, `/cart`, and `/checkout` path redirects exist, but they are unnamed. Legacy components that use named route objects can throw route resolution errors if reused.
  - Legacy `/support` and `/refund-request` paths are not defined in the current router, despite links existing in legacy header/footer/order pages.
