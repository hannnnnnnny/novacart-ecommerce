<template>
  <div class="platform-shell">
    <a class="skip-link" href="#platform-content">Skip to content</a>
    <PlatformNavbar />
    <main id="platform-content" tabindex="-1">
      <RouterView />
    </main>
    <footer class="platform-footer">
      <span>NovaCart</span>
      <nav aria-label="Platform footer">
        <RouterLink to="/features">Features</RouterLink>
        <RouterLink to="/templates">Templates</RouterLink>
        <RouterLink to="/pricing">Pricing</RouterLink>
        <RouterLink to="/login">Merchant login</RouterLink>
      </nav>
    </footer>
  </div>
</template>

<script setup>
import PlatformNavbar from '../components/PlatformNavbar.vue'
</script>
