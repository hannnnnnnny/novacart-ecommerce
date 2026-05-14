<template>
  <div class="admin-shell">
    <AdminSidebar />
    <main class="admin-main">
      <RouterView />
    </main>
  </div>
</template>

<script setup>
import AdminSidebar from '../components/AdminSidebar.vue'
</script>
