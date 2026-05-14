<template>
  <span class="status-badge" :class="variantClass">
    <span class="status-dot" aria-hidden="true"></span>
    {{ label }}
  </span>
</template>

<script setup>
import { computed } from 'vue'
import { formatStatus } from '../utils/format'

const props = defineProps({
  value: {
    type: [String, Boolean],
    required: true
  },
  label: {
    type: String,
    default: ''
  }
})

const normalized = computed(() => String(props.value).toLowerCase())
const label = computed(() => props.label || formatStatus(String(props.value)))
const variantClass = computed(() => {
  if (['cancelled', 'false', 'inactive', 'out of stock'].includes(normalized.value)) return 'status-danger'
  if (['pending', 'processing', 'low stock'].includes(normalized.value)) return 'status-warning'
  if (['paid', 'shipped', 'completed', 'active', 'true', 'in stock'].includes(normalized.value)) return 'status-success'
  return 'status-neutral'
})
</script>
