<template>
  <section class="platform-section">
    <div class="platform-section-heading">
      <p class="eyebrow">Merchant workflow</p>
      <h2>From idea to published storefront</h2>
    </div>
    <ol class="workflow-steps">
      <li v-for="([title, description], index) in steps" :key="title">
        <span>{{ index + 1 }}</span>
        <h3>{{ title }}</h3>
        <p>{{ description }}</p>
      </li>
    </ol>
  </section>
</template>

<script setup>
defineProps({
  steps: {
    type: Array,
    required: true
  }
})
</script>
