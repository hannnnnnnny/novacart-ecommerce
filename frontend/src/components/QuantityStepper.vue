<template>
  <div class="quantity-stepper" :aria-label="label">
    <button type="button" :disabled="modelValue <= min" aria-label="Decrease quantity" @click="update(modelValue - 1)">
      -
    </button>
    <input
      :value="modelValue"
      :min="min"
      :max="max"
      inputmode="numeric"
      type="number"
      aria-label="Quantity"
      @input="update(Number($event.target.value))"
    />
    <button type="button" :disabled="modelValue >= max" aria-label="Increase quantity" @click="update(modelValue + 1)">
      +
    </button>
  </div>
</template>

<script setup>
const props = defineProps({
  modelValue: {
    type: Number,
    required: true
  },
  min: {
    type: Number,
    default: 1
  },
  max: {
    type: Number,
    default: 99
  },
  label: {
    type: String,
    default: 'Quantity selector'
  }
})

const emit = defineEmits(['update:modelValue'])

function update(value) {
  if (!Number.isFinite(value)) return
  emit('update:modelValue', Math.min(Math.max(value, props.min), props.max))
}
</script>
