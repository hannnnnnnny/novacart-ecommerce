<template>
  <aside class="admin-sidebar">
    <RouterLink class="admin-brand" to="/admin/dashboard">NovaCart Admin</RouterLink>
    <nav aria-label="Admin navigation">
      <RouterLink to="/admin/dashboard">Dashboard</RouterLink>
      <RouterLink to="/admin/products">Products</RouterLink>
      <RouterLink to="/admin/categories">Categories</RouterLink>
      <RouterLink to="/admin/orders">Orders</RouterLink>
      <RouterLink to="/admin/inventory">Inventory</RouterLink>
    </nav>
    <button class="secondary-button" type="button" @click="logout">Sign out</button>
  </aside>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { useAuthStore } from '../stores/auth'

const router = useRouter()
const authStore = useAuthStore()

function logout() {
  authStore.clearSession()
  router.push('/admin/login')
}
</script>
