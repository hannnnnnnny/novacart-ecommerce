<template>
  <section class="platform-section">
    <div class="platform-section-heading">
      <p class="eyebrow">Built-in commerce tools</p>
      <h2>Everything a merchant needs to run a store</h2>
      <p>NovaCart combines storefront creation with daily operations, customer care, and performance reporting.</p>
    </div>
    <div class="feature-grid">
      <article v-for="([title, description], index) in features" :key="title" class="feature-card" :data-index="String(index + 1).padStart(2, '0')">
        <span class="feature-icon" aria-hidden="true">
          <component :is="featureIcons[index % featureIcons.length]" />
        </span>
        <h3>{{ title }}</h3>
        <p>{{ description }}</p>
      </article>
    </div>
  </section>
</template>

<script setup>
import { BadgePercent, BarChart3, Boxes, Headphones, LayoutTemplate, PackageSearch, ReceiptText } from 'lucide-vue-next'

defineProps({
  features: {
    type: Array,
    required: true
  }
})

const featureIcons = [LayoutTemplate, PackageSearch, ReceiptText, Boxes, BadgePercent, BarChart3, Headphones]
</script>
