<template>
  <section class="admin-page">
    <PageHeader
      eyebrow="Operations"
      title="Dashboard"
      description="Monitor fashion catalog health, orders, revenue, and inventory warnings from one overview."
    />
    <LoadingState v-if="loading" message="Loading dashboard..." />
    <ErrorMessage v-else-if="error" :message="error" />
    <div v-else class="dashboard-layout">
      <section class="admin-command-hero">
        <div>
          <p class="eyebrow">Merchant command center</p>
          <h2>Today’s retail pulse across revenue, fulfillment, refunds, support, and stock risk.</h2>
          <p>Use the shortcuts below to move quickly from signal to action without leaving the operations workspace.</p>
        </div>
        <div class="command-hero-stats">
          <article>
            <span>Total Revenue</span>
            <strong>{{ formatCurrency(metrics.revenue) }}</strong>
          </article>
          <article>
            <span>Orders</span>
            <strong>{{ metrics.totalOrders || 0 }}</strong>
          </article>
          <article>
            <span>Low Stock</span>
            <strong>{{ warnings.length }}</strong>
          </article>
        </div>
      </section>
      <div class="metric-grid">
        <MetricCard label="Total Products" :value="metrics.totalProducts || 0" detail="All catalog records" />
        <MetricCard label="Active Products" :value="metrics.activeProducts || 0" detail="Visible in storefront" />
        <MetricCard label="Categories" :value="metrics.totalCategories || 0" detail="Catalog groups" />
        <MetricCard label="Total Orders" :value="metrics.totalOrders || 0" detail="Orders received" />
        <MetricCard label="Pending Orders" :value="metrics.pendingOrders || 0" detail="Need review" />
        <MetricCard label="Revenue" :value="formatCurrency(metrics.revenue)" detail="Excluding cancelled orders" />
        <MetricCard label="Daily Sales" :value="formatCurrency(analytics.dailySales)" detail="Last 24 hours" />
        <MetricCard label="Weekly Sales" :value="formatCurrency(analytics.weeklySales)" detail="Last 7 days" />
        <MetricCard label="Monthly Sales" :value="formatCurrency(analytics.monthlySales)" detail="Last 30 days" />
        <MetricCard label="Yearly Sales" :value="formatCurrency(analytics.yearlySales)" detail="Last 365 days" />
        <MetricCard label="Average Order Value" :value="formatCurrency(analytics.averageOrderValue)" detail="Paid orders" />
        <MetricCard label="Refund Requests" :value="analytics.refundRequests || 0" detail="Customer care queue" />
      </div>

      <section class="operations-strip" aria-label="Admin shortcuts">
        <RouterLink to="/admin/products">
          <strong>Manage Catalog</strong>
          <span>Create fashion products, adjust stock, and control visibility.</span>
        </RouterLink>
        <RouterLink to="/admin/orders">
          <strong>Review Orders</strong>
          <span>Move new orders through fulfillment.</span>
        </RouterLink>
        <RouterLink to="/admin/inventory">
          <strong>Check Inventory</strong>
          <span>Find low-stock products before checkout fails.</span>
        </RouterLink>
      </section>

      <section class="dashboard-section">
        <div class="admin-page-header">
          <h2>Sales Trend</h2>
          <RouterLink class="text-link" to="/admin/analytics">Open analytics</RouterLink>
        </div>
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
          <p v-for="region in analytics.topRegions" :key="region.label" class="summary-line">
            <span>{{ region.label }}</span><strong>{{ formatCurrency(region.revenue) }}</strong>
          </p>
          <p v-if="!analytics.topRegions?.length" class="muted">Regional demand appears after checkout.</p>
        </article>
        <article>
          <h2>Customer Preferences</h2>
          <p v-for="preference in analytics.customerPreferenceOverview" :key="preference.label" class="summary-line">
            <span>{{ preference.label }}</span><strong>{{ preference.count }}</strong>
          </p>
          <p v-if="!analytics.customerPreferenceOverview?.length" class="muted">Size, color, and category signals appear after orders.</p>
        </article>
        <article>
          <h2>Best Sellers</h2>
          <p v-for="product in analytics.bestSellingProducts" :key="product.productId" class="summary-line">
            <span>{{ product.productName }}</span><strong>{{ product.unitsSold }}</strong>
          </p>
          <p v-if="!analytics.bestSellingProducts?.length" class="muted">Best sellers appear after checkout.</p>
        </article>
      </section>

      <section class="dashboard-section">
        <div class="admin-page-header">
          <h2>Recent Orders</h2>
          <RouterLink class="text-link" to="/admin/orders">View all</RouterLink>
        </div>
        <EmptyState v-if="!recentOrders.length" title="No orders yet" message="Orders will appear here after checkout." />
        <div v-else class="admin-table-wrap">
          <table class="admin-table compact-table">
            <thead>
              <tr>
                <th>Order</th>
                <th>Customer</th>
                <th>Status</th>
                <th>Total</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="order in recentOrders" :key="order.id">
                <td><RouterLink class="text-link" :to="`/admin/orders/${order.id}`">#{{ order.id }}</RouterLink></td>
                <td>{{ order.customerName }}</td>
                <td><StatusBadge :value="order.status" /></td>
                <td>{{ formatCurrency(order.totalAmount) }}</td>
              </tr>
            </tbody>
          </table>
        </div>
      </section>

      <section class="dashboard-section">
        <div class="admin-page-header">
          <h2>Inventory Watch</h2>
          <RouterLink class="text-link" to="/admin/inventory">Review inventory</RouterLink>
        </div>
        <EmptyState
          v-if="!warnings.length"
          title="Inventory is healthy"
          message="No products are at or below the default warning threshold."
        />
        <div v-else class="warning-grid compact-warning-grid">
          <article v-for="warning in warnings" :key="warning.productId" class="warning-card">
            <div>
              <p class="eyebrow">{{ warning.categoryName }}</p>
              <h3>{{ warning.productName }}</h3>
            </div>
            <strong>{{ warning.stockQuantity }} left</strong>
          </article>
        </div>
      </section>
    </div>
  </section>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import { fetchAdminAnalytics, fetchAdminOrders, fetchDashboardMetrics, fetchInventoryWarnings } from '../../api/admin'
import { getApiError } from '../../api/client'
import EmptyState from '../../components/EmptyState.vue'
import ErrorMessage from '../../components/ErrorMessage.vue'
import LoadingState from '../../components/LoadingState.vue'
import MetricCard from '../../components/MetricCard.vue'
import PageHeader from '../../components/PageHeader.vue'
import StatusBadge from '../../components/StatusBadge.vue'
import { formatCurrency } from '../../utils/format'

const loading = ref(true)
const error = ref('')
const metrics = ref({})
const analytics = ref({})
const orders = ref([])
const warnings = ref([])
const recentOrders = computed(() => orders.value.slice(0, 5))
const maxTrendRevenue = computed(() => {
  return Math.max(...(analytics.value.salesTrend || []).map((point) => Number(point.revenue)), 1)
})

onMounted(async () => {
  try {
    const [metricData, analyticsData, orderData, warningData] = await Promise.all([
      fetchDashboardMetrics(),
      fetchAdminAnalytics(),
      fetchAdminOrders(),
      fetchInventoryWarnings(5)
    ])
    metrics.value = metricData
    analytics.value = analyticsData
    orders.value = orderData
    warnings.value = warningData
  } catch (requestError) {
    error.value = getApiError(requestError, 'Dashboard metrics could not be loaded.')
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
