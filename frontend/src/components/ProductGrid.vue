<template>
  <div class="product-grid generated-product-grid">
    <article v-for="product in products" :key="product.id" class="product-card generated-product-card">
      <RouterLink class="product-image-frame" :to="`/store/${store.slug}/products/${product.id}`">
        <img :src="product.imageUrl" :alt="product.name" />
        <span v-if="primaryBadge(product)" class="product-image-badge">{{ primaryBadge(product) }}</span>
      </RouterLink>
      <div class="product-card-body">
        <p class="eyebrow">{{ product.category }}</p>
        <RouterLink class="product-card-title" :to="`/store/${store.slug}/products/${product.id}`">
          <h3>{{ product.name }}</h3>
        </RouterLink>
        <p class="product-card-description">{{ product.description }}</p>
        <div class="price-stack">
          <strong>{{ formatCurrency(product.price) }}</strong>
          <span v-if="product.compareAtPrice">{{ formatCurrency(product.compareAtPrice) }}</span>
        </div>
        <div class="card-actions retail-card-actions">
          <button class="primary-button product-card-cta" type="button" :disabled="product.stockQuantity < 1" @click="$emit('add', product)">
            {{ product.stockQuantity < 1 ? 'Unavailable' : 'Add to cart' }}
          </button>
          <RouterLink class="secondary-button product-card-secondary" :to="`/store/${store.slug}/products/${product.id}`">View</RouterLink>
        </div>
      </div>
    </article>
  </div>
</template>

<script setup>
import { formatCurrency } from '../utils/format'

defineProps({
  products: {
    type: Array,
    required: true
  },
  store: {
    type: Object,
    required: true
  }
})

defineEmits(['add'])

function primaryBadge(product) {
  if (product.discountPercent) return `${product.discountPercent}% off`
  if (product.stockQuantity <= (product.lowStockThreshold || 5)) return 'Low stock'
  return product.badges?.[0] || ''
}
</script>
