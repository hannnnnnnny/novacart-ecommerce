<template>
  <section class="page-section">
    <header class="section-heading">
      <p class="eyebrow">Basket</p>
      <h1>Cart</h1>
    </header>
    <EmptyState v-if="!cartStore.items.length" title="Your cart is empty" message="Add products to begin checkout.">
      <RouterLink class="primary-button" to="/products">Browse Products</RouterLink>
    </EmptyState>
    <div v-else class="cart-layout">
      <article v-for="item in cartStore.items" :key="item.productId" class="cart-item">
        <img :src="item.imageUrl" :alt="item.name" />
        <div>
          <h2>{{ item.name }}</h2>
          <p>{{ formatCurrency(item.price) }}</p>
          <input
            type="number"
            min="1"
            :max="item.stockQuantity"
            :value="item.quantity"
            @input="cartStore.updateQuantity(item.productId, Number($event.target.value))"
          />
          <button class="text-button" type="button" @click="cartStore.removeItem(item.productId)">Remove</button>
        </div>
      </article>
      <aside class="summary-panel">
        <h2>Order Summary</h2>
        <p>Subtotal: {{ formatCurrency(cartStore.subtotal) }}</p>
        <RouterLink class="primary-button" to="/checkout">Continue to Checkout</RouterLink>
      </aside>
    </div>
  </section>
</template>

<script setup>
import { onMounted } from 'vue'
import EmptyState from '../components/EmptyState.vue'
import { useCartStore } from '../stores/cart'
import { formatCurrency } from '../utils/format'

const cartStore = useCartStore()

onMounted(() => {
  cartStore.loadCart()
})
</script>
