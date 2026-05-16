<template>
  <section class="admin-page">
    <PageHeader
      eyebrow="Customer Care"
      title="Refund Requests"
      description="Review refund requests, approve or reject them, and keep payment/refund status synchronized."
    />
    <div class="admin-toolbar">
      <label>Status
        <select v-model="statusFilter" @change="loadRefunds">
          <option value="">All statuses</option>
          <option value="REQUESTED">Requested</option>
          <option value="UNDER_REVIEW">Under review</option>
          <option value="APPROVED">Approved</option>
          <option value="REJECTED">Rejected</option>
          <option value="REFUNDED">Refunded</option>
        </select>
      </label>
    </div>
    <LoadingState v-if="loading" message="Loading refund requests..." />
    <ErrorMessage v-else-if="error" :message="error" />
    <EmptyState v-else-if="!refunds.length" title="No refund requests" message="Eligible customer refund requests will appear here." />
    <div v-else class="admin-table-wrap">
      <table class="admin-table">
        <thead><tr><th>Order</th><th>Customer</th><th>Reason</th><th>Status</th><th>Update</th></tr></thead>
        <tbody>
          <tr v-for="refund in refunds" :key="refund.id">
            <td><RouterLink class="text-link" :to="`/admin/orders/${refund.orderId}`">{{ refund.orderNumber }}</RouterLink></td>
            <td><strong>{{ refund.customerName }}</strong><span>{{ refund.email }}</span></td>
            <td>{{ refund.reason }}</td>
            <td><StatusBadge :value="refund.status" /></td>
            <td>
              <div class="table-actions">
                <select v-model="refund.nextStatus">
                  <option value="REQUESTED">Requested</option>
                  <option value="UNDER_REVIEW">Under review</option>
                  <option value="APPROVED">Approved</option>
                  <option value="REJECTED">Rejected</option>
                  <option value="REFUNDED">Refunded</option>
                </select>
                <button class="secondary-button compact-button" type="button" @click="updateRefund(refund)">Save</button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </section>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { fetchAdminRefunds, updateAdminRefund } from '../../api/admin'
import { getApiError } from '../../api/client'
import EmptyState from '../../components/EmptyState.vue'
import ErrorMessage from '../../components/ErrorMessage.vue'
import LoadingState from '../../components/LoadingState.vue'
import PageHeader from '../../components/PageHeader.vue'
import StatusBadge from '../../components/StatusBadge.vue'

const loading = ref(true)
const error = ref('')
const statusFilter = ref('')
const refunds = ref([])

onMounted(loadRefunds)

async function loadRefunds() {
  loading.value = true
  error.value = ''
  try {
    const params = statusFilter.value ? { status: statusFilter.value } : {}
    refunds.value = (await fetchAdminRefunds(params)).map((refund) => ({ ...refund, nextStatus: refund.status }))
  } catch (requestError) {
    error.value = getApiError(requestError, 'Refund requests could not be loaded.')
  } finally {
    loading.value = false
  }
}

async function updateRefund(refund) {
  try {
    const updated = await updateAdminRefund(refund.id, { status: refund.nextStatus, internalNotes: refund.internalNotes || null })
    Object.assign(refund, updated, { nextStatus: updated.status })
  } catch (requestError) {
    error.value = getApiError(requestError, 'Refund request could not be updated.')
  }
}
</script>
