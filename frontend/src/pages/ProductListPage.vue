<template>
  <section class="page-section">
    <PageHeader
      eyebrow="Storefront"
      title="Products"
      description="Search, filter, and compare the current catalog without losing your place."
    />
    <div class="catalog-toolbar">
      <label class="search-field">
        Search products
        <input v-model.trim="searchTerm" type="search" placeholder="Search by name or description" />
      </label>
      <label>
        Sort by
        <select v-model="sortMode">
          <option value="name">Name</option>
          <option value="price-low">Price: low to high</option>
          <option value="price-high">Price: high to low</option>
          <option value="stock">Stock availability</option>
        </select>
      </label>
    </div>
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
    <div v-else-if="filteredProducts.length">
      <p class="result-count">{{ filteredProducts.length }} products shown</p>
      <div class="product-grid">
        <ProductCard
          v-for="product in filteredProducts"
          :key="product.id"
          :product="product"
          @added="showAddedMessage"
        />
      </div>
    </div>
    <EmptyState
      v-else
      title="No matching products"
      message="Try clearing the search field or choosing a different category."
    />
    <ToastMessage :message="toastMessage" />
  </section>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import { useRoute } from 'vue-router'
import { fetchCategories, fetchProducts } from '../api/catalog'
import EmptyState from '../components/EmptyState.vue'
import ErrorMessage from '../components/ErrorMessage.vue'
import LoadingState from '../components/LoadingState.vue'
import PageHeader from '../components/PageHeader.vue'
import ProductCard from '../components/ProductCard.vue'
import ToastMessage from '../components/ToastMessage.vue'
import { getApiError } from '../api/client'

const route = useRoute()
const loading = ref(true)
const error = ref('')
const products = ref([])
const categories = ref([])
const selectedCategoryId = ref(null)
const searchTerm = ref('')
const sortMode = ref('name')
const toastMessage = ref('')
let toastTimer
const filteredProducts = computed(() => {
  const query = searchTerm.value.toLowerCase()
  const searchedProducts = query
    ? products.value.filter((product) => {
        return `${product.name} ${product.description} ${product.category?.name || ''}`.toLowerCase().includes(query)
      })
    : [...products.value]

  return searchedProducts.sort((left, right) => {
    if (sortMode.value === 'price-low') return Number(left.price) - Number(right.price)
    if (sortMode.value === 'price-high') return Number(right.price) - Number(left.price)
    if (sortMode.value === 'stock') return right.stockQuantity - left.stockQuantity
    return left.name.localeCompare(right.name)
  })
})

onMounted(async () => {
  const categoryFromQuery = Number(route.query.category)
  selectedCategoryId.value = Number.isFinite(categoryFromQuery) && categoryFromQuery > 0 ? categoryFromQuery : null
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

function showAddedMessage(product) {
  toastMessage.value = `${product.name} added to cart.`
  window.clearTimeout(toastTimer)
  toastTimer = window.setTimeout(() => {
    toastMessage.value = ''
  }, 2600)
}
</script>
