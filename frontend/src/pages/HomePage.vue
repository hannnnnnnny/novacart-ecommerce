<template>
  <div>
    <section class="hero-section">
      <div class="hero-copy">
        <p class="eyebrow">Useful goods, carefully organized</p>
        <h1>NovaCart Ecommerce</h1>
        <p>Shop considered home, studio, and daily essentials through a quiet storefront built for fast decisions and reliable checkout.</p>
        <div class="hero-actions">
          <RouterLink class="primary-button" to="/products">Shop Products</RouterLink>
          <RouterLink class="secondary-button hero-secondary" to="/cart">View Cart</RouterLink>
        </div>
      </div>
      <div class="hero-stats" aria-label="Storefront highlights">
        <article>
          <strong>{{ products.length || '6' }}</strong>
          <span>Catalog items</span>
        </article>
        <article>
          <strong>{{ categories.length || '4' }}</strong>
          <span>Curated categories</span>
        </article>
        <article>
          <strong>24/7</strong>
          <span>Online ordering</span>
        </article>
      </div>
    </section>

    <section class="page-section category-section">
      <SectionHeader
        eyebrow="Shop by use"
        title="Find the right fit faster"
        description="Category highlights keep browsing clear, purposeful, and easy to scan."
      />
      <LoadingState v-if="loading" message="Loading categories..." />
      <ErrorMessage v-else-if="error" :message="error" />
      <div v-else class="category-highlight-grid">
        <RouterLink
          v-for="category in categories"
          :key="category.id"
          class="category-highlight"
          :to="{ name: 'products', query: { category: category.id } }"
        >
          <span>{{ category.name }}</span>
          <small>{{ category.description }}</small>
        </RouterLink>
      </div>
    </section>

    <section class="page-section featured-section">
      <SectionHeader
        eyebrow="Fresh from the catalog"
        title="Featured Products"
        description="A few practical favorites from the current NovaCart sample inventory."
      />
      <LoadingState v-if="loading" message="Loading featured products..." />
      <ErrorMessage v-else-if="error" :message="error" />
      <div v-else class="product-grid">
        <ProductCard v-for="product in featuredProducts" :key="product.id" :product="product" />
      </div>
    </section>

    <section class="page-section value-section">
      <div class="value-grid">
        <article v-for="item in valueCards" :key="item.title" class="value-card">
          <component :is="item.icon" aria-hidden="true" />
          <h2>{{ item.title }}</h2>
          <p>{{ item.description }}</p>
        </article>
      </div>
    </section>
  </div>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import { ShieldCheck, Sparkles, Truck } from 'lucide-vue-next'
import { fetchCategories, fetchProducts } from '../api/catalog'
import { getApiError } from '../api/client'
import ErrorMessage from '../components/ErrorMessage.vue'
import LoadingState from '../components/LoadingState.vue'
import ProductCard from '../components/ProductCard.vue'
import SectionHeader from '../components/SectionHeader.vue'

const loading = ref(true)
const error = ref('')
const products = ref([])
const categories = ref([])
const featuredProducts = computed(() => products.value.slice(0, 3))
const valueCards = [
  {
    title: 'Fast Checkout',
    description: 'A short, focused checkout flow keeps orders moving without extra friction.',
    icon: Truck
  },
  {
    title: 'Curated Products',
    description: 'Categories are built around practical needs, not endless browsing noise.',
    icon: Sparkles
  },
  {
    title: 'Secure Orders',
    description: 'Admin access is protected and order data is handled through a consistent API.',
    icon: ShieldCheck
  }
]

onMounted(async () => {
  try {
    const [categoryData, productData] = await Promise.all([fetchCategories(), fetchProducts()])
    categories.value = categoryData
    products.value = productData
  } catch (requestError) {
    error.value = getApiError(requestError, 'Storefront content could not be loaded.')
  } finally {
    loading.value = false
  }
})
</script>
