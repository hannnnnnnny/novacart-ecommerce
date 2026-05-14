<template>
  <article class="metric-card">
    <span>{{ label }}</span>
    <strong>{{ value }}</strong>
    <small v-if="detail">{{ detail }}</small>
  </article>
</template>

<script setup>
defineProps({
  label: {
    type: String,
    required: true
  },
  value: {
    type: [String, Number],
    required: true
  },
  detail: {
    type: String,
    default: ''
  }
})
</script>
