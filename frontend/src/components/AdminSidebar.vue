<template>
  <aside class="admin-sidebar">
    <RouterLink class="admin-brand" to="/admin/dashboard" aria-label="NovaCart admin dashboard">
      <span class="brand-mark" aria-hidden="true">N</span>
      <span>NovaCart Admin</span>
    </RouterLink>

    <nav class="admin-nav" aria-label="Admin navigation">
      <RouterLink to="/admin/dashboard">Dashboard</RouterLink>
      <RouterLink to="/admin/analytics">Analytics</RouterLink>
      <RouterLink to="/admin/products">Products</RouterLink>
      <RouterLink to="/admin/categories">Categories</RouterLink>
      <RouterLink to="/admin/collections">Collections</RouterLink>
      <RouterLink to="/admin/promotions">Promotions</RouterLink>
      <RouterLink to="/admin/orders">Orders</RouterLink>
      <RouterLink to="/admin/customers">Customers</RouterLink>
      <RouterLink to="/admin/refunds">Refunds</RouterLink>
      <RouterLink to="/admin/support">Support</RouterLink>
      <RouterLink to="/admin/inventory">Inventory</RouterLink>
    </nav>

    <div class="admin-session-card">
      <span>Signed in</span>
      <strong>{{ authStore.email || 'Administrator' }}</strong>
    </div>

    <button class="secondary-button admin-signout" type="button" @click="logout">Sign out</button>
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
