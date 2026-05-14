<template>
  <article class="product-card">
    <RouterLink :to="`/products/${product.id}`">
      <img :src="product.imageUrl" :alt="product.name" />
    </RouterLink>
    <div>
      <div class="product-card-meta">
        <p class="eyebrow">{{ product.category?.name }}</p>
        <StatusBadge :value="stockStatus" :label="stockLabel" />
      </div>
      <RouterLink class="product-card-title" :to="`/products/${product.id}`">
        <h3>{{ product.name }}</h3>
      </RouterLink>
      <p>{{ product.description }}</p>
      <div class="card-row">
        <strong>{{ formatCurrency(product.price) }}</strong>
        <div class="card-actions">
          <RouterLink class="text-link" :to="`/products/${product.id}`">Details</RouterLink>
          <button
            v-if="showCartAction"
            class="secondary-button compact-button"
            type="button"
            :disabled="product.stockQuantity < 1"
            @click="addProduct"
          >
            Add
          </button>
        </div>
      </div>
    </div>
  </article>
</template>

<script setup>
import { computed } from 'vue'
import StatusBadge from './StatusBadge.vue'
import { useCartStore } from '../stores/cart'
import { formatCurrency } from '../utils/format'

const props = defineProps({
  product: {
    type: Object,
    required: true
  },
  showCartAction: {
    type: Boolean,
    default: true
  }
})

const emit = defineEmits(['added'])
const cartStore = useCartStore()
const stockStatus = computed(() => {
  if (props.product.stockQuantity < 1) return 'out of stock'
  if (props.product.stockQuantity <= 5) return 'low stock'
  return 'in stock'
})
const stockLabel = computed(() => {
  if (props.product.stockQuantity < 1) return 'Out of stock'
  if (props.product.stockQuantity <= 5) return 'Low stock'
  return 'In stock'
})

function addProduct() {
  cartStore.addItem(props.product, 1)
  emit('added', props.product)
}
</script>
