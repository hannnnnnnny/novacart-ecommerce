<template>
  <div class="admin-shell">
    <a class="skip-link" href="#admin-content">Skip to admin content</a>
    <AdminSidebar />
    <main id="admin-content" class="admin-main" tabindex="-1">
      <RouterView />
    </main>
  </div>
</template>

<script setup>
import AdminSidebar from '../components/AdminSidebar.vue'
</script>
