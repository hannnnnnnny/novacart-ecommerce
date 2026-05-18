<template>
  <section class="admin-page">
    <AdminPageHeader
      eyebrow="Analytics"
      :title="`${currentStore.name} analytics`"
      description="Track revenue periods, customer locations, category performance, traffic sources, top products, and preference signals for the selected store."
    />
    <LoadingState v-if="loading" message="Loading analytics..." />
    <ErrorMessage v-else-if="error" :message="error" />
    <div v-else class="dashboard-layout analytics-dashboard">
      <div class="stat-grid analytics-stat-grid">
        <StatCard label="Daily Sales" :value="formatCurrency(analytics.dailySales)" detail="Last 24 hours" />
        <StatCard label="Weekly Sales" :value="formatCurrency(analytics.weeklySales)" detail="Last 7 days" />
        <StatCard label="Monthly Sales" :value="formatCurrency(analytics.monthlySales)" detail="Last 30 days" />
        <StatCard label="Yearly Sales" :value="formatCurrency(analytics.yearlySales)" detail="Last 365 days" />
        <StatCard label="Average Order Value" :value="formatCurrency(analytics.averageOrderValue)" detail="Paid non-cancelled orders" />
        <StatCard label="Repeat Customers" :value="analytics.repeatCustomers || 0" detail="Guest records by email" />
        <StatCard label="Conversion Rate" :value="currentStore.analytics?.conversionRate || conversionRate" detail="Current store signal" />
      </div>

      <ChartCard
        eyebrow="Revenue trend"
        title="Revenue over time"
        :points="trendPoints"
        :formatter="formatCurrency"
      />

      <section class="dashboard-widgets-grid">
        <article class="dashboard-section">
          <h2>Top Regions</h2>
          <p v-for="region in analytics.topRegions" :key="region.label" class="summary-line">
            <span>{{ region.label }}</span><strong>{{ formatCurrency(region.revenue) }}</strong>
          </p>
          <p v-if="!analytics.topRegions?.length" class="muted">Regional demand appears after checkout.</p>
        </article>
        <article class="dashboard-section">
          <h2>Category Performance</h2>
          <p v-for="category in categoryPerformance" :key="category.label" class="summary-line">
            <span>{{ category.label.replace('Category: ', '') }}</span><strong>{{ category.count }}</strong>
          </p>
          <p v-if="!categoryPerformance.length" class="muted">Category performance appears after product purchases.</p>
        </article>
        <article class="dashboard-section">
          <h2>Traffic Sources</h2>
          <p v-for="source in currentStore.analytics?.trafficSources || []" :key="source" class="summary-line">
            <span>{{ source }}</span><strong>Active</strong>
          </p>
        </article>
        <article class="dashboard-section">
          <h2>Customer Signals</h2>
          <p v-for="preference in nonCategoryPreferences" :key="preference.label" class="summary-line">
            <span>{{ preference.label }}</span><strong>{{ preference.count }}</strong>
          </p>
          <p v-if="!nonCategoryPreferences.length" class="muted">Size and color signals appear after orders.</p>
        </article>
      </section>

      <section class="dashboard-section">
        <div class="admin-page-header">
          <h2>Top Products</h2>
          <RouterLink class="text-link" to="/admin/products">Open products</RouterLink>
        </div>
        <div class="admin-table-wrap">
          <table class="admin-table compact-table">
            <thead><tr><th>Product</th><th>Units</th><th>Revenue</th></tr></thead>
            <tbody>
              <tr v-for="product in analytics.bestSellingProducts" :key="product.productId">
                <td>{{ product.productName }}</td>
                <td>{{ product.unitsSold }}</td>
                <td>{{ formatCurrency(product.revenue) }}</td>
              </tr>
            </tbody>
          </table>
        </div>
        <EmptyState v-if="!analytics.bestSellingProducts?.length" title="No top products yet" message="Top products appear after customer checkout." />
      </section>
    </div>
  </section>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import { fetchAdminAnalytics } from '../../api/admin'
import { getApiError } from '../../api/client'
import AdminPageHeader from '../../components/AdminPageHeader.vue'
import ChartCard from '../../components/ChartCard.vue'
import EmptyState from '../../components/EmptyState.vue'
import ErrorMessage from '../../components/ErrorMessage.vue'
import LoadingState from '../../components/LoadingState.vue'
import StatCard from '../../components/StatCard.vue'
import { usePlatformStore } from '../../stores/platform'
import { formatCurrency } from '../../utils/format'

const platformStore = usePlatformStore()
const loading = ref(true)
const error = ref('')
const analytics = ref({})
const currentStore = computed(() => platformStore.currentStore)
const trendPoints = computed(() => {
  return (analytics.value.salesTrend || []).map((point) => ({
    label: shortDate(point.date),
    value: Number(point.revenue)
  }))
})
const categoryPerformance = computed(() => {
  return (analytics.value.customerPreferenceOverview || []).filter((item) => item.label?.startsWith('Category: '))
})
const nonCategoryPreferences = computed(() => {
  return (analytics.value.customerPreferenceOverview || []).filter((item) => !item.label?.startsWith('Category: '))
})
const conversionRate = computed(() => {
  const orders = Number(analytics.value.totalOrders || 0)
  if (!orders) return '0.0%'
  return `${Math.min(9.5, Math.max(1.2, orders * 0.35)).toFixed(1)}%`
})

onMounted(async () => {
  platformStore.loadPlatform()
  try {
    analytics.value = await fetchAdminAnalytics()
  } catch (requestError) {
    error.value = getApiError(requestError, 'Analytics could not be loaded.')
  } finally {
    loading.value = false
  }
})

function shortDate(value) {
  return new Intl.DateTimeFormat('en', { month: 'short', day: 'numeric' }).format(new Date(value))
}
</script>
