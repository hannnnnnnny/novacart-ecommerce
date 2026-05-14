<template>
  <section class="page-section">
    <header class="section-heading">
      <p class="eyebrow">Storefront</p>
      <h1>Products</h1>
    </header>
    <LoadingState v-if="loading" message="Loading products..." />
    <ErrorMessage v-else-if="error" :message="error" />
    <div v-else-if="products.length" class="product-grid">
      <ProductCard v-for="product in products" :key="product.id" :product="product" />
    </div>
    <EmptyState v-else title="No products available" message="Active products will appear here after they are added." />
  </section>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { fetchProducts } from '../api/catalog'
import EmptyState from '../components/EmptyState.vue'
import ErrorMessage from '../components/ErrorMessage.vue'
import LoadingState from '../components/LoadingState.vue'
import ProductCard from '../components/ProductCard.vue'
import { getApiError } from '../api/client'

const loading = ref(true)
const error = ref('')
const products = ref([])

onMounted(async () => {
  try {
    products.value = await fetchProducts()
  } catch (requestError) {
    error.value = getApiError(requestError, 'Products could not be loaded.')
  } finally {
    loading.value = false
  }
})
</script>
