<template>
  <section class="page-section">
    <LoadingState v-if="loading" message="Loading order..." />
    <ErrorMessage v-else-if="error" :message="error" />
    <div v-else class="success-panel">
      <p class="eyebrow">Order Confirmed</p>
      <h1>Thank you, {{ order.customerName }}</h1>
      <p>Your order #{{ order.id }} has been received and is currently {{ order.status.toLowerCase() }}.</p>
      <RouterLink class="primary-button" to="/products">Continue Shopping</RouterLink>
    </div>
  </section>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { useRoute } from 'vue-router'
import { fetchOrder } from '../api/orders'
import { getApiError } from '../api/client'
import ErrorMessage from '../components/ErrorMessage.vue'
import LoadingState from '../components/LoadingState.vue'

const route = useRoute()
const loading = ref(true)
const error = ref('')
const order = ref(null)

onMounted(async () => {
  try {
    order.value = await fetchOrder(route.params.id)
  } catch (requestError) {
    error.value = getApiError(requestError, 'Order could not be loaded.')
  } finally {
    loading.value = false
  }
})
</script>
