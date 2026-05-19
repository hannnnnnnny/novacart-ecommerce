<template>
  <section class="platform-hero">
    <div class="platform-hero-copy">
      <p class="eyebrow">NovaCart ecommerce website builder</p>
      <h1>Launch a store that already feels merchandised.</h1>
      <p>Build the storefront, catalog, orders, care queue, and growth dashboard from one workspace designed for real retail rhythm.</p>
      <div class="hero-actions">
        <RouterLink class="primary-button" to="/onboarding">Start building</RouterLink>
        <RouterLink class="secondary-button" to="/templates">View templates</RouterLink>
      </div>
      <dl class="platform-proof-list" aria-label="NovaCart launch signals">
        <div>
          <dt>4</dt>
          <dd>demo storefronts</dd>
        </div>
        <div>
          <dt>24</dt>
          <dd>seed products</dd>
        </div>
        <div>
          <dt>1</dt>
          <dd>merchant console</dd>
        </div>
      </dl>
    </div>
    <div class="platform-hero-preview" aria-label="NovaCart platform preview">
      <div class="preview-window-bar" aria-hidden="true">
        <div class="browser-dots"><span></span><span></span><span></span></div>
        <span>merchant-console/live</span>
      </div>
      <div class="preview-commerce-board">
        <div class="preview-store-card">
          <img src="/demo-images/heroes/fashion.jpg" alt="Fashion store template preview" />
          <div>
            <span>Template live</span>
            <strong>Avery Studio</strong>
            <small>Fashion Boutique</small>
          </div>
        </div>
        <div class="preview-kpi-row">
          <article><span>Sales</span><strong>$18.4k</strong></article>
          <article><span>Orders</span><strong>214</strong></article>
          <article><span>Conversion</span><strong>3.8%</strong></article>
        </div>
        <ul class="preview-ops-list" aria-label="Operations preview">
          <li><span>Catalog</span><strong>Low-stock blazer restock queued</strong></li>
          <li><span>Care</span><strong>Refund review under 30-day window</strong></li>
          <li><span>Launch</span><strong>Spring edit announcement active</strong></li>
        </ul>
      </div>
    </div>
  </section>
</template>
