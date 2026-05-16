<template>
  <section class="admin-page">
    <PageHeader
      eyebrow="Analytics"
      title="Customer and Sales Analytics"
      description="Track revenue periods, regional demand, customer preferences, and top products."
    />
    <LoadingState v-if="loading" message="Loading analytics..." />
    <ErrorMessage v-else-if="error" :message="error" />
    <div v-else class="dashboard-layout">
      <div class="metric-grid">
        <MetricCard label="Daily Sales" :value="formatCurrency(analytics.dailySales)" detail="Last 24 hours" />
        <MetricCard label="Weekly Sales" :value="formatCurrency(analytics.weeklySales)" detail="Last 7 days" />
        <MetricCard label="Monthly Sales" :value="formatCurrency(analytics.monthlySales)" detail="Last 30 days" />
        <MetricCard label="Yearly Sales" :value="formatCurrency(analytics.yearlySales)" detail="Last 365 days" />
        <MetricCard label="Average Order Value" :value="formatCurrency(analytics.averageOrderValue)" detail="Paid non-cancelled orders" />
        <MetricCard label="Repeat Customers" :value="analytics.repeatCustomers || 0" detail="Guest records by email" />
      </div>
      <section class="dashboard-section">
        <h2>Sales Trend</h2>
        <div class="trend-bars">
          <article v-for="point in analytics.salesTrend" :key="point.date" class="trend-bar">
            <span>{{ shortDate(point.date) }}</span>
            <div><i :style="{ height: barHeight(point.revenue) }"></i></div>
            <strong>{{ formatCurrency(point.revenue) }}</strong>
          </article>
        </div>
      </section>
      <section class="dashboard-section analytics-grid">
        <article>
          <h2>Top Regions</h2>
          <p v-for="region in analytics.topRegions" :key="region.label" class="summary-line"><span>{{ region.label }}</span><strong>{{ formatCurrency(region.revenue) }}</strong></p>
        </article>
        <article>
          <h2>Customer Preferences</h2>
          <p v-for="preference in analytics.customerPreferenceOverview" :key="preference.label" class="summary-line"><span>{{ preference.label }}</span><strong>{{ preference.count }}</strong></p>
        </article>
        <article>
          <h2>Best Sellers</h2>
          <p v-for="product in analytics.bestSellingProducts" :key="product.productId" class="summary-line"><span>{{ product.productName }}</span><strong>{{ product.unitsSold }}</strong></p>
        </article>
      </section>
    </div>
  </section>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import { fetchAdminAnalytics } from '../../api/admin'
import { getApiError } from '../../api/client'
import ErrorMessage from '../../components/ErrorMessage.vue'
import LoadingState from '../../components/LoadingState.vue'
import MetricCard from '../../components/MetricCard.vue'
import PageHeader from '../../components/PageHeader.vue'
import { formatCurrency } from '../../utils/format'

const loading = ref(true)
const error = ref('')
const analytics = ref({})
const maxTrendRevenue = computed(() => {
  return Math.max(...(analytics.value.salesTrend || []).map((point) => Number(point.revenue)), 1)
})

onMounted(async () => {
  try {
    analytics.value = await fetchAdminAnalytics()
  } catch (requestError) {
    error.value = getApiError(requestError, 'Analytics could not be loaded.')
  } finally {
    loading.value = false
  }
})

function barHeight(value) {
  return `${Math.max(8, (Number(value) / maxTrendRevenue.value) * 120)}px`
}

function shortDate(value) {
  return new Intl.DateTimeFormat('en', { month: 'short', day: 'numeric' }).format(new Date(value))
}
</script>
