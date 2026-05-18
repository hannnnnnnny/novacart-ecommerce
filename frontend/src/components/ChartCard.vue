<template>
  <section class="chart-card">
    <div class="chart-card-header">
      <div>
        <p v-if="eyebrow" class="eyebrow">{{ eyebrow }}</p>
        <h2>{{ title }}</h2>
      </div>
      <slot name="actions" />
    </div>
    <div v-if="points.length" class="chart-bars" :aria-label="title">
      <article v-for="point in points" :key="point.label" class="chart-bar-item">
        <div><i :style="{ height: barHeight(point.value) }"></i></div>
        <span>{{ point.label }}</span>
        <strong>{{ formatValue(point.value) }}</strong>
      </article>
    </div>
    <EmptyState v-else title="No chart data yet" message="Chart data appears after orders are created." />
  </section>
</template>

<script setup>
import { computed } from 'vue'
import EmptyState from './EmptyState.vue'

const props = defineProps({
  eyebrow: {
    type: String,
    default: ''
  },
  title: {
    type: String,
    required: true
  },
  points: {
    type: Array,
    default: () => []
  },
  formatter: {
    type: Function,
    default: (value) => value
  }
})

const maxValue = computed(() => {
  return Math.max(...props.points.map((point) => Number(point.value)), 1)
})

function barHeight(value) {
  return `${Math.max(10, (Number(value) / maxValue.value) * 136)}px`
}

function formatValue(value) {
  return props.formatter(value)
}
</script>
