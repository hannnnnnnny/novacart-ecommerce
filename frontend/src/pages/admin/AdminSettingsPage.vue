<template>
  <section class="admin-page">
    <AdminPageHeader
      eyebrow="Store configuration"
      title="Settings"
      description="Review store profile, admin security, environment details, and future theme controls."
    />

    <div class="settings-grid">
      <article class="settings-card">
        <h2>Store Profile</h2>
        <dl>
          <div><dt>Store name</dt><dd>NovaCart Fashion Commerce</dd></div>
          <div><dt>Primary channel</dt><dd>Online storefront</dd></div>
          <div><dt>Catalog focus</dt><dd>Fashion, thrift finds, accessories, sportswear, lifestyle extras</dd></div>
          <div><dt>Demo currency</dt><dd>USD display formatting</dd></div>
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
        <h2>Theme & Operations Roadmap</h2>
        <p class="muted">
          Theme controls, real media uploads, tax configuration, channel-specific settings, and notification preferences are intentionally presented as roadmap items until backend support is added.
        </p>
        <div class="settings-placeholder-list">
          <span>Theme presets</span>
          <span>Upload storage</span>
          <span>Tax profiles</span>
          <span>Notification rules</span>
        </div>
      </article>
    </div>
  </section>
</template>

<script setup>
import AdminPageHeader from '../../components/AdminPageHeader.vue'
import { useAuthStore } from '../../stores/auth'

const authStore = useAuthStore()
const apiBaseUrl = import.meta.env.VITE_API_BASE_URL || '/api'
</script>
