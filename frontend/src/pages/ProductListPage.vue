<template>
  <section class="page-section">
    <header class="section-heading">
      <p class="eyebrow">Storefront</p>
      <h1>Products</h1>
    </header>
    <div class="filter-row" aria-label="Product categories">
      <button class="filter-button" :class="{ active: selectedCategoryId === null }" type="button" @click="selectCategory(null)">
        All Products
      </button>
      <button
        v-for="category in categories"
        :key="category.id"
        class="filter-button"
        :class="{ active: selectedCategoryId === category.id }"
        type="button"
        @click="selectCategory(category.id)"
      >
        {{ category.name }}
      </button>
    </div>
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
import { fetchCategories, fetchProducts } from '../api/catalog'
import EmptyState from '../components/EmptyState.vue'
import ErrorMessage from '../components/ErrorMessage.vue'
import LoadingState from '../components/LoadingState.vue'
import ProductCard from '../components/ProductCard.vue'
import { getApiError } from '../api/client'

const loading = ref(true)
const error = ref('')
const products = ref([])
const categories = ref([])
const selectedCategoryId = ref(null)

onMounted(async () => {
  await loadProducts()
})

async function loadProducts() {
  loading.value = true
  error.value = ''
  try {
    const [categoryData, productData] = await Promise.all([
      categories.value.length ? categories.value : fetchCategories(),
      fetchProducts(selectedCategoryId.value)
    ])
    categories.value = categoryData
    products.value = productData
  } catch (requestError) {
    error.value = getApiError(requestError, 'Products could not be loaded.')
  } finally {
    loading.value = false
  }
}

function selectCategory(categoryId) {
  selectedCategoryId.value = categoryId
  loadProducts()
}
</script>
