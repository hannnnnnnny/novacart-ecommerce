<template>
  <div class="state-panel">
    <h2>{{ title }}</h2>
    <p>{{ message }}</p>
    <slot />
  </div>
</template>

<script setup>
defineProps({
  title: {
    type: String,
    default: 'Nothing to show yet'
  },
  message: {
    type: String,
    default: 'Content will appear here when it is available.'
  }
})
</script>
