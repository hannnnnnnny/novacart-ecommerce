<template>
  <section class="platform-section">
    <div class="platform-section-heading">
      <p class="eyebrow">Built-in commerce tools</p>
      <h2>Everything a merchant needs to run a store</h2>
      <p>NovaCart combines storefront creation with daily operations, customer care, and performance reporting.</p>
    </div>
    <div class="feature-grid">
      <article v-for="[title, description] in features" :key="title" class="feature-card">
        <span class="feature-icon" aria-hidden="true">{{ title.slice(0, 1) }}</span>
        <h3>{{ title }}</h3>
        <p>{{ description }}</p>
      </article>
    </div>
  </section>
</template>

<script setup>
defineProps({
  features: {
    type: Array,
    required: true
  }
})
</script>
