<template>
  <div class="state-panel">
    <div class="spinner" aria-hidden="true"></div>
    <p>{{ message }}</p>
  </div>
</template>

<script setup>
defineProps({
  message: {
    type: String,
    default: 'Loading content...'
  }
})
</script>
