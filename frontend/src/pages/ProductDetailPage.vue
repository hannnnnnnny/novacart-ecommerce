<template>
  <section class="page-section">
    <LoadingState v-if="loading" message="Loading product..." />
    <ErrorMessage v-else-if="error" :message="error" />
    <div v-else class="detail-layout">
      <img :src="product.imageUrl" :alt="product.name" />
      <div>
        <p class="eyebrow">{{ product.category?.name }}</p>
        <h1>{{ product.name }}</h1>
        <p>{{ product.description }}</p>
        <strong class="price">{{ formatCurrency(product.price) }}</strong>
        <p class="muted">{{ product.stockQuantity }} in stock</p>
        <button class="primary-button" type="button" :disabled="product.stockQuantity < 1" @click="addToCart">
          Add to Cart
        </button>
      </div>
    </div>
  </section>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { fetchProduct } from '../api/catalog'
import { getApiError } from '../api/client'
import ErrorMessage from '../components/ErrorMessage.vue'
import LoadingState from '../components/LoadingState.vue'
import { useCartStore } from '../stores/cart'
import { formatCurrency } from '../utils/format'

const route = useRoute()
const router = useRouter()
const cartStore = useCartStore()
const loading = ref(true)
const error = ref('')
const product = ref(null)

onMounted(async () => {
  try {
    product.value = await fetchProduct(route.params.id)
  } catch (requestError) {
    error.value = getApiError(requestError, 'Product could not be loaded.')
  } finally {
    loading.value = false
  }
})

function addToCart() {
  cartStore.addItem(product.value, 1)
  router.push('/cart')
}
</script>
