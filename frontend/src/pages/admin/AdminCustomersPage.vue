<template>
  <section class="admin-page">
    <PageHeader
      eyebrow="Customers"
      title="Guest Customer Records"
      description="Review checkout-created customer records, location signals, repeat ordering, and average order value."
    />
    <div class="admin-toolbar">
      <label class="search-field">
        Search customers
        <input v-model.trim="searchTerm" type="search" placeholder="Search by name, email, city, region, or country" />
      </label>
    </div>
    <LoadingState v-if="loading" message="Loading customers..." />
    <ErrorMessage v-else-if="error" :message="error" />
    <EmptyState
      v-else-if="!filteredCustomers.length"
      title="No customer records"
      message="Guest customer profiles are created automatically after checkout."
    />
    <div v-else class="customer-management-grid">
      <div class="admin-table-wrap">
        <table class="admin-table">
          <thead>
            <tr>
              <th>Customer</th>
              <th>Location</th>
              <th>Orders</th>
              <th>Total Spent</th>
              <th>Average Order</th>
              <th>Last Order</th>
              <th>Detail</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="customer in filteredCustomers" :key="customer.id" :class="{ selected: selectedCustomer?.id === customer.id }">
              <td>
                <strong>{{ customer.name }}</strong>
                <span>{{ customer.email }}</span>
                <span v-if="customer.phone">{{ customer.phone }}</span>
              </td>
              <td>
                <strong>{{ locationLabel(customer) }}</strong>
                <span>{{ customer.addressSummary || 'Address summary not available' }}</span>
              </td>
              <td>{{ customer.totalOrders }}</td>
              <td>{{ formatCurrency(customer.totalSpent) }}</td>
              <td>{{ formatCurrency(customer.averageOrderValue) }}</td>
              <td>{{ formatDate(customer.lastOrderAt) }}</td>
              <td><button class="text-button" type="button" @click="selectedCustomer = customer">View</button></td>
            </tr>
          </tbody>
        </table>
      </div>
      <aside class="customer-detail-panel">
        <h2>Customer detail</h2>
        <template v-if="selectedCustomer">
          <strong>{{ selectedCustomer.name }}</strong>
          <span>{{ selectedCustomer.email }}</span>
          <p class="muted">{{ selectedCustomer.phone || 'No phone number provided.' }}</p>
          <dl>
            <div><dt>Orders</dt><dd>{{ selectedCustomer.totalOrders }}</dd></div>
            <div><dt>Total spent</dt><dd>{{ formatCurrency(selectedCustomer.totalSpent) }}</dd></div>
            <div><dt>Average order</dt><dd>{{ formatCurrency(selectedCustomer.averageOrderValue) }}</dd></div>
            <div><dt>Last order</dt><dd>{{ formatDate(selectedCustomer.lastOrderAt) }}</dd></div>
            <div><dt>Location</dt><dd>{{ locationLabel(selectedCustomer) }}</dd></div>
          </dl>
        </template>
        <p v-else class="muted">Select a customer to review location, order count, spend, and last order signals.</p>
      </aside>
    </div>
  </section>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import { fetchAdminCustomers } from '../../api/admin'
import { getApiError } from '../../api/client'
import EmptyState from '../../components/EmptyState.vue'
import ErrorMessage from '../../components/ErrorMessage.vue'
import LoadingState from '../../components/LoadingState.vue'
import PageHeader from '../../components/PageHeader.vue'
import { formatCurrency, formatDate } from '../../utils/format'

const loading = ref(true)
const error = ref('')
const customers = ref([])
const searchTerm = ref('')
const selectedCustomer = ref(null)
const filteredCustomers = computed(() => {
  const query = searchTerm.value.toLowerCase()
  if (!query) return customers.value
  return customers.value.filter((customer) => {
    return [
      customer.name,
      customer.email,
      customer.phone,
      customer.city,
      customer.region,
      customer.country,
      customer.addressSummary
    ].filter(Boolean).join(' ').toLowerCase().includes(query)
  })
})

onMounted(loadCustomers)

async function loadCustomers() {
  loading.value = true
  error.value = ''
  try {
    customers.value = await fetchAdminCustomers()
    selectedCustomer.value = customers.value[0] || null
  } catch (requestError) {
    error.value = getApiError(requestError, 'Customers could not be loaded.')
  } finally {
    loading.value = false
  }
}

function locationLabel(customer) {
  return [customer.city, customer.region, customer.country].filter(Boolean).join(', ') || 'Location not available'
}
</script>
