<template>
  <section class="setup-checklist-card">
    <div class="admin-page-header">
      <div>
        <p class="eyebrow">Store setup</p>
        <h2>{{ store.name }} launch checklist</h2>
      </div>
      <RouterLink class="text-link" to="/admin/store-setup">Open setup</RouterLink>
    </div>
    <ol>
      <li v-for="item in checklist" :key="item.key" :class="{ complete: item.complete }">
        <span>{{ item.complete ? 'Done' : 'Next' }}</span>
        <div>
          <strong>{{ item.label }}</strong>
          <p>{{ item.description }}</p>
        </div>
      </li>
    </ol>
  </section>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  store: {
    type: Object,
    required: true
  }
})

const checklist = computed(() => [
  { key: 'details', label: 'Add store details', description: 'Name, description, category, and public slug.', complete: props.store.setup?.details },
  { key: 'template', label: 'Choose template', description: 'Select the storefront style for this merchant.', complete: props.store.setup?.template },
  { key: 'products', label: 'Add products', description: 'Create the first sellable catalog items.', complete: props.store.setup?.products || props.store.products?.length },
  { key: 'shipping', label: 'Set shipping', description: 'Add the customer-facing shipping promise.', complete: props.store.setup?.shipping },
  { key: 'preview', label: 'Preview store', description: 'Check the generated customer storefront.', complete: props.store.setup?.preview },
  { key: 'publish', label: 'Publish store', description: 'Make the merchant storefront available.', complete: props.store.published || props.store.setup?.publish }
])
</script>
