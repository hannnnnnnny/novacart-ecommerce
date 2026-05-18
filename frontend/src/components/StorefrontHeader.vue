<template>
  <header class="generated-store-header">
    <RouterLink class="generated-store-brand" :to="`/store/${store.slug}`">
      <span :style="{ background: store.brandColor }">{{ store.logoText }}</span>
      <strong>{{ store.name }}</strong>
    </RouterLink>
    <nav aria-label="Store categories">
      <RouterLink v-for="category in store.categories.slice(0, 6)" :key="category" :to="{ path: `/store/${store.slug}/products`, query: { category } }">
        {{ category }}
      </RouterLink>
    </nav>
    <div class="generated-store-actions">
      <RouterLink :to="`/store/${store.slug}/products`">Shop</RouterLink>
      <RouterLink class="cart-link" :to="`/store/${store.slug}/cart`">Cart {{ itemCount }}</RouterLink>
    </div>
  </header>
</template>

<script setup>
import { computed } from 'vue'
import { useStorefrontCartStore } from '../stores/storefrontCart'

const props = defineProps({
  store: {
    type: Object,
    required: true
  }
})

const cartStore = useStorefrontCartStore()
const itemCount = computed(() => cartStore.itemCountForStore(props.store.slug))
</script>
