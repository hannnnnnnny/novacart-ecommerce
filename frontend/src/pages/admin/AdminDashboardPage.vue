<template>
  <section class="admin-page">
    <h1>Dashboard</h1>
    <LoadingState v-if="loading" message="Loading dashboard..." />
    <ErrorMessage v-else-if="error" :message="error" />
    <div v-else class="metrics-grid">
      <article><span>Total Products</span><strong>{{ metrics.totalProducts }}</strong></article>
      <article><span>Active Products</span><strong>{{ metrics.activeProducts }}</strong></article>
      <article><span>Total Orders</span><strong>{{ metrics.totalOrders }}</strong></article>
      <article><span>Revenue</span><strong>{{ formatCurrency(metrics.revenue) }}</strong></article>
    </div>
  </section>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { fetchDashboardMetrics } from '../../api/admin'
import { getApiError } from '../../api/client'
import ErrorMessage from '../../components/ErrorMessage.vue'
import LoadingState from '../../components/LoadingState.vue'
import { formatCurrency } from '../../utils/format'

const loading = ref(true)
const error = ref('')
const metrics = ref({})

onMounted(async () => {
  try {
    metrics.value = await fetchDashboardMetrics()
  } catch (requestError) {
    error.value = getApiError(requestError, 'Dashboard metrics could not be loaded.')
  } finally {
    loading.value = false
  }
})
</script>
