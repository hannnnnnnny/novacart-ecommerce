<template>
  <header class="app-header">
    <RouterLink class="brand" to="/">NovaCart</RouterLink>
    <nav aria-label="Storefront navigation">
      <RouterLink to="/products">Products</RouterLink>
      <RouterLink to="/cart">Cart ({{ cartStore.itemCount }})</RouterLink>
      <RouterLink to="/admin/dashboard">Admin</RouterLink>
    </nav>
  </header>
</template>

<script setup>
import { onMounted } from 'vue'
import { useCartStore } from '../stores/cart'

const cartStore = useCartStore()

onMounted(() => {
  cartStore.loadCart()
})
</script>
