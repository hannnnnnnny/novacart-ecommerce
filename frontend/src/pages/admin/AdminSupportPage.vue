<template>
  <section class="admin-page">
    <PageHeader
      eyebrow="Customer Care"
      title="Support Tickets"
      description="Review customer service issues and move tickets through the support workflow."
    />
    <LoadingState v-if="loading" message="Loading support tickets..." />
    <ErrorMessage v-else-if="error" :message="error" />
    <EmptyState v-else-if="!tickets.length" title="No support tickets" message="Customer support requests will appear here." />
    <div v-else class="admin-table-wrap">
      <table class="admin-table">
        <thead><tr><th>Customer</th><th>Issue</th><th>Message</th><th>Status</th><th>Update</th></tr></thead>
        <tbody>
          <tr v-for="ticket in tickets" :key="ticket.id">
            <td><strong>{{ ticket.customerName }}</strong><span>{{ ticket.email }} {{ ticket.orderNumber || '' }}</span></td>
            <td>{{ formatStatus(ticket.issueType) }}</td>
            <td>{{ ticket.message }}</td>
            <td><StatusBadge :value="ticket.status" /></td>
            <td>
              <div class="table-actions">
                <select v-model="ticket.nextStatus">
                  <option value="OPEN">Open</option>
                  <option value="IN_REVIEW">In review</option>
                  <option value="WAITING_FOR_CUSTOMER">Waiting for customer</option>
                  <option value="RESOLVED">Resolved</option>
                  <option value="CLOSED">Closed</option>
                </select>
                <button class="secondary-button compact-button" type="button" @click="updateTicket(ticket)">Save</button>
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
import { fetchAdminSupportTickets, updateAdminSupportTicket } from '../../api/admin'
import { getApiError } from '../../api/client'
import EmptyState from '../../components/EmptyState.vue'
import ErrorMessage from '../../components/ErrorMessage.vue'
import LoadingState from '../../components/LoadingState.vue'
import PageHeader from '../../components/PageHeader.vue'
import StatusBadge from '../../components/StatusBadge.vue'
import { formatStatus } from '../../utils/format'

const loading = ref(true)
const error = ref('')
const tickets = ref([])

onMounted(loadTickets)

async function loadTickets() {
  loading.value = true
  error.value = ''
  try {
    tickets.value = (await fetchAdminSupportTickets()).map((ticket) => ({ ...ticket, nextStatus: ticket.status }))
  } catch (requestError) {
    error.value = getApiError(requestError, 'Support tickets could not be loaded.')
  } finally {
    loading.value = false
  }
}

async function updateTicket(ticket) {
  try {
    const updated = await updateAdminSupportTicket(ticket.id, { status: ticket.nextStatus, internalNotes: ticket.internalNotes || null })
    Object.assign(ticket, updated, { nextStatus: updated.status })
  } catch (requestError) {
    error.value = getApiError(requestError, 'Support ticket could not be updated.')
  }
}
</script>
