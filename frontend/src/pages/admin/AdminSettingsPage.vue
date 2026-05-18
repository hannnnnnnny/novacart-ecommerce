<template>
  <section class="admin-page">
    <AdminPageHeader
      eyebrow="Merchant settings"
      :title="`${store.name} settings`"
      description="Manage the current store profile, slug, currency, shipping message, security context, and platform environment."
    />

    <div class="settings-grid">
      <article class="settings-card">
        <h2>Store Profile</h2>
        <dl>
          <div><dt>Store name</dt><dd>{{ store.name }}</dd></div>
          <div><dt>Public storefront</dt><dd>/store/{{ store.slug }}</dd></div>
          <div><dt>Catalog focus</dt><dd>{{ store.category }}</dd></div>
          <div><dt>Demo currency</dt><dd>{{ store.currency }}</dd></div>
          <div><dt>Shipping message</dt><dd>{{ store.shippingMessage }}</dd></div>
        </dl>
      </article>

      <article class="settings-card">
        <h2>Admin Account & Security</h2>
        <dl>
          <div><dt>Signed in as</dt><dd>{{ authStore.email || 'Administrator' }}</dd></div>
          <div><dt>Authentication</dt><dd>JWT-protected admin routes</dd></div>
          <div><dt>Demo admin</dt><dd>admin@novacart.local</dd></div>
          <div><dt>Password storage</dt><dd>Encoded server-side, never exposed in API responses</dd></div>
        </dl>
      </article>

      <article class="settings-card">
        <h2>API & Environment</h2>
        <dl>
          <div><dt>Frontend API base</dt><dd>{{ apiBaseUrl }}</dd></div>
          <div><dt>Payments</dt><dd>Demo-safe only, no real card processing</dd></div>
          <div><dt>Refund window</dt><dd>30 days for paid orders</dd></div>
          <div><dt>Inventory</dt><dd>Stock checked and deducted during checkout</dd></div>
        </dl>
      </article>

      <article class="settings-card">
        <h2>Theme, Payments & Operations</h2>
        <p class="muted">
          Theme controls work locally for the current merchant store. Real media uploads, custom domains, tax automation, and live payment processors are documented roadmap items until backend support is added.
        </p>
        <div class="settings-placeholder-list">
          <RouterLink to="/admin/theme-editor">Theme editor</RouterLink>
          <RouterLink to="/admin/templates">Templates</RouterLink>
          <RouterLink :to="`/store/${store.slug}`">Storefront preview</RouterLink>
          <span>Upload storage</span>
          <span>Tax profiles</span>
          <span>Payment provider setup</span>
        </div>
      </article>
    </div>
  </section>
</template>

<script setup>
import AdminPageHeader from '../../components/AdminPageHeader.vue'
import { useAuthStore } from '../../stores/auth'
import { usePlatformStore } from '../../stores/platform'
import { computed, onMounted } from 'vue'

const authStore = useAuthStore()
const platformStore = usePlatformStore()
const apiBaseUrl = import.meta.env.VITE_API_BASE_URL || '/api'
const store = computed(() => platformStore.currentStore)

onMounted(() => {
  platformStore.loadPlatform()
})
</script>
