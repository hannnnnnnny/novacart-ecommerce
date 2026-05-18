<template>
  <section class="platform-hero">
    <div class="platform-hero-copy">
      <p class="eyebrow">NovaCart ecommerce website builder</p>
      <h1>Launch your online store with NovaCart</h1>
      <p>Choose a template, add your products, manage orders, and grow with built-in analytics.</p>
      <div class="hero-actions">
        <RouterLink class="primary-button" to="/onboarding">Start building</RouterLink>
        <RouterLink class="secondary-button" to="/templates">View templates</RouterLink>
      </div>
    </div>
    <div class="platform-hero-preview" aria-label="NovaCart platform preview">
      <div class="browser-dots" aria-hidden="true"><span></span><span></span><span></span></div>
      <div class="preview-kpi-row">
        <article><span>Sales</span><strong>$18.4k</strong></article>
        <article><span>Orders</span><strong>214</strong></article>
        <article><span>Conversion</span><strong>3.8%</strong></article>
      </div>
      <div class="preview-store-card">
        <img src="/demo-images/heroes/fashion.jpg" alt="Fashion store template preview" />
        <div>
          <strong>Avery Studio</strong>
          <span>Fashion Boutique template</span>
        </div>
      </div>
    </div>
  </section>
</template>
