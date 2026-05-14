<template>
  <div v-if="message" class="toast-message" role="status">
    {{ message }}
  </div>
</template>

<script setup>
defineProps({
  message: {
    type: String,
    default: ''
  }
})
</script>
