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
    <div v-else class="admin-table-wrap">
      <table class="admin-table">
        <thead>
          <tr>
            <th>Customer</th>
            <th>Location</th>
            <th>Orders</th>
            <th>Total Spent</th>
            <th>Average Order</th>
            <th>Last Order</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="customer in filteredCustomers" :key="customer.id">
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
          </tr>
        </tbody>
      </table>
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
