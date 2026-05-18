<template>
  <div class="admin-shell">
    <a class="skip-link" href="#admin-content">Skip to admin content</a>
    <AdminSidebar />
    <div class="admin-workspace">
      <AdminTopbar :notification-count="0" />
      <main id="admin-content" class="admin-main" tabindex="-1">
        <RouterView />
      </main>
    </div>
  </div>
</template>

<script setup>
import { onMounted } from 'vue'
import AdminSidebar from '../components/AdminSidebar.vue'
import AdminTopbar from '../components/AdminTopbar.vue'
import { usePlatformStore } from '../stores/platform'

const platformStore = usePlatformStore()

onMounted(() => {
  platformStore.loadPlatform()
})
</script>
