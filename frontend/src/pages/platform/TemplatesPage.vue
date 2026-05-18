<template>
  <div>
    <section class="platform-page-hero">
      <p class="eyebrow">Templates</p>
      <h1>Choose the starting point for each merchant store</h1>
      <p>Each template creates a different storefront personality while staying under the original NovaCart product system.</p>
    </section>
    <TemplateShowcase :templates="storeTemplates" />
    <section class="platform-section">
      <div class="generated-store-grid">
        <StoreTemplatePreview v-for="store in demoStores" :key="store.slug" :store="store" />
      </div>
    </section>
  </div>
</template>

<script setup>
import StoreTemplatePreview from '../../components/StoreTemplatePreview.vue'
import TemplateShowcase from '../../components/TemplateShowcase.vue'
import { demoStores, storeTemplates } from '../../data/platform'
</script>
