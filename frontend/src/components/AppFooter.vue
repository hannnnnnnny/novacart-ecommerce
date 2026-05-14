<template>
  <footer class="app-footer">
    <div>
      <RouterLink class="brand footer-brand" to="/">
        <span class="brand-mark" aria-hidden="true">N</span>
        <span>NovaCart</span>
      </RouterLink>
      <p>Thoughtful commerce tools for small merchants and curated storefronts.</p>
    </div>

    <nav class="footer-nav" aria-label="Footer navigation">
      <RouterLink to="/products">Browse Products</RouterLink>
      <RouterLink to="/cart">Cart</RouterLink>
      <RouterLink to="/admin/login">Admin Login</RouterLink>
    </nav>
  </footer>
</template>
