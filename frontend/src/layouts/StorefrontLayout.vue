<template>
  <div class="site-shell">
    <a class="skip-link" href="#storefront-content">Skip to content</a>
    <AppHeader />
    <main id="storefront-content" tabindex="-1">
      <RouterView />
    </main>
    <AppFooter />
  </div>
</template>

<script setup>
import AppFooter from '../components/AppFooter.vue'
import AppHeader from '../components/AppHeader.vue'
</script>
