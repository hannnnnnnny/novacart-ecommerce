<template>
  <RouterLink class="quick-action-card" :to="to">
    <component :is="icon" v-if="icon" aria-hidden="true" />
    <strong>{{ title }}</strong>
    <span>{{ description }}</span>
  </RouterLink>
</template>

<script setup>
defineProps({
  title: {
    type: String,
    required: true
  },
  description: {
    type: String,
    required: true
  },
  to: {
    type: [String, Object],
    required: true
  },
  icon: {
    type: [Object, Function],
    default: null
  }
})
</script>
