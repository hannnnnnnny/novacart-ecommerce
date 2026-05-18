<template>
  <section v-if="product" class="page-section merchant-product-detail-page">
    <nav class="product-breadcrumb" aria-label="Product path">
      <RouterLink :to="`/store/${store.slug}`">{{ store.name }}</RouterLink>
      <span>/</span>
      <RouterLink :to="{ path: `/store/${store.slug}/products`, query: { category: product.category } }">{{ product.category }}</RouterLink>
    </nav>
    <div class="merchant-detail-layout">
      <div class="merchant-detail-media">
        <span v-if="product.discountPercent" class="product-image-badge detail-discount-badge">{{ product.discountPercent }}% off</span>
        <img :src="product.imageUrl" :alt="product.name" />
      </div>
      <div class="merchant-detail-buy-panel">
        <p class="eyebrow">{{ product.category }}</p>
        <h1>{{ product.name }}</h1>
        <p class="product-description">{{ product.description }}</p>
        <div class="price-stack detail-price">
          <strong class="price">{{ formatCurrency(product.price) }}</strong>
          <span v-if="product.compareAtPrice">{{ formatCurrency(product.compareAtPrice) }}</span>
        </div>
        <p class="muted">{{ product.stockQuantity }} available from {{ store.name }}. Demo-safe checkout, no real payment captured.</p>
        <label>Quantity<QuantityStepper v-model="quantity" :max="Math.max(product.stockQuantity, 1)" /></label>
        <div class="card-actions">
          <button class="primary-button" type="button" :disabled="product.stockQuantity < 1" @click="addToCart">Add to cart</button>
          <button class="secondary-button" type="button" :disabled="product.stockQuantity < 1" @click="buyNow">Buy now</button>
        </div>
        <div class="merchant-detail-notes">
          <span>{{ store.shippingMessage }}</span>
          <span>30-day refund request window</span>
        </div>
      </div>
    </div>
    <section class="related-section">
      <div class="retail-section-heading">
        <p class="eyebrow">More from {{ store.name }}</p>
        <h2>Related products</h2>
      </div>
      <ProductGrid :products="relatedProducts" :store="store" @add="addRelated" />
    </section>
    <ToastMessage :message="toastMessage" />
  </section>
  <EmptyState v-else title="Product not found" message="This product is not available in the merchant storefront.">
    <RouterLink class="primary-button" :to="`/store/${store.slug}/products`">Back to products</RouterLink>
  </EmptyState>
</template>

<script setup>
import { computed, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import EmptyState from '../../components/EmptyState.vue'
import ProductGrid from '../../components/ProductGrid.vue'
import QuantityStepper from '../../components/QuantityStepper.vue'
import ToastMessage from '../../components/ToastMessage.vue'
import { useStorefrontCartStore } from '../../stores/storefrontCart'
import { formatCurrency } from '../../utils/format'

const props = defineProps({
  store: {
    type: Object,
    required: true
  }
})

const route = useRoute()
const router = useRouter()
const cartStore = useStorefrontCartStore()
const quantity = ref(1)
const toastMessage = ref('')
let toastTimer
const product = computed(() => props.store.products.find((entry) => String(entry.id) === String(route.params.productId)))
const relatedProducts = computed(() => props.store.products.filter((entry) => entry.id !== product.value?.id).slice(0, 3))

function addToCart() {
  if (!product.value) return
  cartStore.addItem(props.store.slug, product.value, quantity.value)
  toastMessage.value = `${product.value.name} added to cart.`
  window.clearTimeout(toastTimer)
  toastTimer = window.setTimeout(() => {
    toastMessage.value = ''
  }, 2400)
}

function addRelated(relatedProduct) {
  cartStore.addItem(props.store.slug, relatedProduct, 1)
  toastMessage.value = `${relatedProduct.name} added to cart.`
}

function buyNow() {
  addToCart()
  router.push(`/store/${props.store.slug}/checkout`)
}
</script>
