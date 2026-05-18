<template>
  <header class="admin-topbar">
    <form class="admin-global-search" role="search" @submit.prevent="submitSearch">
      <SearchInput
        v-model="searchTerm"
        label="Search admin"
        placeholder="Search products, orders, customers"
        @submit="submitSearch"
      />
    </form>
    <div class="admin-topbar-controls">
      <label>
        <CalendarDays aria-hidden="true" />
        <select v-model="dateRange">
          <option>Last 7 days</option>
          <option>Last 30 days</option>
          <option>Last 90 days</option>
          <option>This year</option>
        </select>
      </label>
      <label>
        <PanelsTopLeft aria-hidden="true" />
        <select v-model="channel">
          <option>All channels</option>
          <option>Online storefront</option>
          <option>Manual orders</option>
          <option>Support assisted</option>
        </select>
      </label>
      <RouterLink class="admin-notification-button" to="/admin/support" aria-label="Open support queue">
        <Bell aria-hidden="true" />
        <span v-if="notificationCount">{{ notificationCount }}</span>
      </RouterLink>
      <RouterLink class="admin-profile-chip" to="/admin/settings">
        <span class="profile-avatar">N</span>
        <span>
          <strong>NovaCart</strong>
          <small>{{ authStore.email || 'Administrator' }}</small>
        </span>
      </RouterLink>
    </div>
  </header>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { Bell, CalendarDays, PanelsTopLeft } from 'lucide-vue-next'
import SearchInput from './SearchInput.vue'
import { useAuthStore } from '../stores/auth'

defineProps({
  notificationCount: {
    type: Number,
    default: 0
  }
})

const router = useRouter()
const authStore = useAuthStore()
const searchTerm = ref('')
const dateRange = ref('Last 30 days')
const channel = ref('All channels')

function submitSearch() {
  const search = searchTerm.value.trim()
  if (!search) return
  router.push({ name: 'admin-products', query: { search } })
}
</script>
