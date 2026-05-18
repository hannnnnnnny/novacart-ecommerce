<template>
  <section class="admin-page">
    <AdminPageHeader
      eyebrow="Store builder"
      title="Templates"
      description="Preview and select an original NovaCart storefront template for the current merchant store."
    >
      <template #actions>
        <RouterLink class="secondary-button" :to="`/store/${store.slug}`">Preview storefront</RouterLink>
      </template>
    </AdminPageHeader>
    <div class="template-showcase-grid admin-template-grid">
      <article v-for="template in platformStore.templates" :key="template.id" class="template-card" :class="{ active: store.template === template.id }">
        <img :src="template.previewImage" :alt="`${template.name} preview`" />
        <div>
          <h2>{{ template.name }}</h2>
          <p>{{ template.description }}</p>
          <span>Best for: {{ template.bestFor }}</span>
        </div>
        <div class="template-actions">
          <RouterLink class="secondary-button" :to="{ path: `/store/${store.slug}`, query: { templatePreview: template.id } }">Preview</RouterLink>
          <button class="primary-button" type="button" :disabled="store.template === template.id" @click="selectTemplate(template.id)">
            {{ store.template === template.id ? 'Selected' : 'Select template' }}
          </button>
        </div>
      </article>
    </div>
  </section>
</template>

<script setup>
import { computed, onMounted } from 'vue'
import AdminPageHeader from '../../components/AdminPageHeader.vue'
import { getTemplateById } from '../../data/platform'
import { usePlatformStore } from '../../stores/platform'

const platformStore = usePlatformStore()
const store = computed(() => platformStore.currentStore)

onMounted(() => platformStore.loadPlatform())

function selectTemplate(templateId) {
  const template = getTemplateById(templateId)
  platformStore.updateStore(store.value.slug, {
    template: template.id,
    brandColor: store.value.brandColor || template.accentColor,
    setup: { template: true }
  })
}
</script>
