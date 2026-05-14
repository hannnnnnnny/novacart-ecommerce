<template>
  <div>
    <section class="hero-section">
      <div class="hero-copy">
        <p class="eyebrow">Independent commerce, neatly run</p>
        <h1>NovaCart Ecommerce</h1>
        <p>Browse practical goods from a focused catalog and check out with a clean, reliable shopping flow.</p>
        <RouterLink class="primary-button" to="/products">Shop Products</RouterLink>
      </div>
    </section>
    <section class="page-section featured-section">
      <header class="section-heading">
        <p class="eyebrow">Fresh from the catalog</p>
        <h2>Featured Products</h2>
      </header>
      <LoadingState v-if="loading" message="Loading featured products..." />
      <ErrorMessage v-else-if="error" :message="error" />
      <div v-else class="product-grid">
        <ProductCard v-for="product in featuredProducts" :key="product.id" :product="product" />
      </div>
    </section>
  </div>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import { fetchProducts } from '../api/catalog'
import { getApiError } from '../api/client'
import ErrorMessage from '../components/ErrorMessage.vue'
import LoadingState from '../components/LoadingState.vue'
import ProductCard from '../components/ProductCard.vue'

const loading = ref(true)
const error = ref('')
const products = ref([])
const featuredProducts = computed(() => products.value.slice(0, 3))

onMounted(async () => {
  try {
    products.value = await fetchProducts()
  } catch (requestError) {
    error.value = getApiError(requestError, 'Featured products could not be loaded.')
  } finally {
    loading.value = false
  }
})
</script>
