<template>
  <aside class="admin-sidebar professional-sidebar">
    <RouterLink class="admin-brand" to="/admin/dashboard" aria-label="NovaCart admin dashboard">
      <span class="brand-mark" aria-hidden="true">N</span>
      <span>
        <strong>NovaCart</strong>
        <small>Merchant Admin</small>
      </span>
    </RouterLink>

    <nav class="admin-nav" aria-label="Admin navigation">
      <RouterLink v-for="item in navItems" :key="item.label" :to="item.to">
        <component :is="item.icon" aria-hidden="true" />
        <span>{{ item.label }}</span>
      </RouterLink>
    </nav>

    <div class="admin-session-card">
      <span>Signed in</span>
      <strong>{{ authStore.email || 'Administrator' }}</strong>
    </div>

    <button class="secondary-button admin-signout" type="button" @click="logout">Sign out</button>
  </aside>
</template>

<script setup>
import {
  BarChart3,
  Boxes,
  Gauge,
  Gift,
  Headphones,
  Home,
  Layers3,
  Package,
  ReceiptText,
  RotateCcw,
  Settings,
  Tag,
  Users
} from 'lucide-vue-next'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../stores/auth'

const router = useRouter()
const authStore = useAuthStore()
const navItems = [
  { label: 'Home / Dashboard', to: '/admin/dashboard', icon: Home },
  { label: 'Orders', to: '/admin/orders', icon: ReceiptText },
  { label: 'Products', to: '/admin/products', icon: Package },
  { label: 'Collections', to: '/admin/collections', icon: Layers3 },
  { label: 'Customers', to: '/admin/customers', icon: Users },
  { label: 'Marketing / Promotions', to: '/admin/promotions', icon: Gift },
  { label: 'Discounts', to: { path: '/admin/promotions', query: { view: 'discounts' } }, icon: Tag },
  { label: 'Inventory', to: '/admin/inventory', icon: Boxes },
  { label: 'Support', to: '/admin/support', icon: Headphones },
  { label: 'Refunds', to: '/admin/refunds', icon: RotateCcw },
  { label: 'Analytics', to: '/admin/analytics', icon: BarChart3 },
  { label: 'Settings', to: '/admin/settings', icon: Settings },
  { label: 'Storefront', to: '/', icon: Gauge }
]

function logout() {
  authStore.clearSession()
  router.push('/admin/login')
}
</script>
