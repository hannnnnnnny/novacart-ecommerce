<template>
  <article class="product-card">
    <RouterLink :to="`/products/${product.id}`">
      <img :src="product.imageUrl" :alt="product.name" />
    </RouterLink>
    <div>
      <p class="eyebrow">{{ product.category?.name }}</p>
      <h3>{{ product.name }}</h3>
      <p>{{ product.description }}</p>
      <div class="card-row">
        <strong>{{ formatCurrency(product.price) }}</strong>
        <RouterLink class="text-link" :to="`/products/${product.id}`">View</RouterLink>
      </div>
    </div>
  </article>
</template>

<script setup>
import { formatCurrency } from '../utils/format'

defineProps({
  product: {
    type: Object,
    required: true
  }
})
</script>
