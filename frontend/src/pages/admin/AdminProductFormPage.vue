<template>
  <section class="admin-page">
    <PageHeader
      eyebrow="Catalog editor"
      :title="isEditing ? 'Edit Product' : 'New Product'"
      description="Keep product details complete, clear, and ready for customers to scan."
    >
      <template #actions>
        <RouterLink class="secondary-button" to="/admin/products">Back to Products</RouterLink>
      </template>
    </PageHeader>
    <LoadingState v-if="loading" message="Loading product form..." />
    <form v-else class="product-editor-layout" @submit.prevent="submitProduct">
      <div class="admin-form">
        <ErrorMessage v-if="error" :message="error" />
        <label>
          Name
          <input v-model.trim="form.name" required maxlength="180" placeholder="Product name" />
        </label>
        <label>
          Slug
          <input v-model.trim="form.slug" maxlength="200" placeholder="optional-product-slug" />
        </label>
        <label>
          Category
          <select v-model.number="form.categoryId" required>
            <option disabled value="">Select a category</option>
            <option v-for="category in categories" :key="category.id" :value="category.id">
              {{ category.name }}
            </option>
          </select>
        </label>
        <label>
          Price
          <input v-model.number="form.price" required min="0.01" step="0.01" type="number" />
        </label>
        <label>
          Stock Quantity
          <input v-model.number="form.stockQuantity" required min="0" step="1" type="number" />
        </label>
        <label>
          Image URL
          <input v-model.trim="form.imageUrl" required placeholder="https://example.com/product.jpg" />
        </label>
        <label class="wide-field">
          Description
          <textarea v-model.trim="form.description" required maxlength="2000" rows="6" placeholder="Describe the product clearly."></textarea>
        </label>
      </div>
      <aside class="summary-panel product-publish-panel">
        <h2>Publishing</h2>
        <p class="muted">Products marked active appear in the public storefront after saving.</p>
        <label class="checkbox-field">
          <input v-model="form.active" type="checkbox" />
          Active in storefront
        </label>
        <div class="summary-line">
          <span>Slug preview</span>
          <strong>{{ slugPreview }}</strong>
        </div>
        <div class="summary-line">
          <span>Stock status</span>
          <StatusBadge :value="stockStatus" :label="stockLabel" />
        </div>
        <button class="primary-button" type="submit" :disabled="submitting || !formIsValid">
          {{ submitting ? 'Saving Product...' : 'Save Product' }}
        </button>
      </aside>
    </form>
  </section>
</template>

<script setup>
import { computed, onMounted, reactive, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import {
  createAdminProduct,
  fetchAdminCategories,
  fetchAdminProduct,
  updateAdminProduct
} from '../../api/admin'
import { getApiError } from '../../api/client'
import ErrorMessage from '../../components/ErrorMessage.vue'
import LoadingState from '../../components/LoadingState.vue'
import PageHeader from '../../components/PageHeader.vue'
import StatusBadge from '../../components/StatusBadge.vue'

const route = useRoute()
const router = useRouter()
const loading = ref(true)
const submitting = ref(false)
const error = ref('')
const categories = ref([])
const isEditing = computed(() => Boolean(route.params.id))
const form = reactive({
  name: '',
  slug: '',
  description: '',
  price: 0,
  stockQuantity: 0,
  imageUrl: '',
  active: true,
  categoryId: ''
})
const formIsValid = computed(() => {
  return form.name
    && form.description
    && Number(form.price) > 0
    && Number(form.stockQuantity) >= 0
    && form.imageUrl
    && form.categoryId
})
const slugPreview = computed(() => {
  const source = form.slug || form.name || 'new-product'
  return source
    .normalize('NFD')
    .replace(/\p{M}/gu, '')
    .toLowerCase()
    .replace(/[^a-z0-9]+/g, '-')
    .replace(/(^-|-$)/g, '')
})
const stockStatus = computed(() => {
  if (Number(form.stockQuantity) < 1) return 'out of stock'
  if (Number(form.stockQuantity) <= 5) return 'low stock'
  return 'in stock'
})
const stockLabel = computed(() => {
  if (Number(form.stockQuantity) < 1) return 'Out of stock'
  if (Number(form.stockQuantity) <= 5) return 'Low stock'
  return 'In stock'
})

onMounted(async () => {
  try {
    categories.value = await fetchAdminCategories()
    if (isEditing.value) {
      const product = await fetchAdminProduct(route.params.id)
      form.name = product.name
      form.slug = product.slug
      form.description = product.description
      form.price = Number(product.price)
      form.stockQuantity = product.stockQuantity
      form.imageUrl = product.imageUrl
      form.active = product.active
      form.categoryId = product.category.id
    }
  } catch (requestError) {
    error.value = getApiError(requestError, 'Product form could not be loaded.')
  } finally {
    loading.value = false
  }
})

async function submitProduct() {
  error.value = ''
  submitting.value = true

  const payload = {
    ...form,
    price: Number(form.price),
    stockQuantity: Number(form.stockQuantity),
    slug: form.slug || null
  }

  try {
    if (isEditing.value) {
      await updateAdminProduct(route.params.id, payload)
    } else {
      await createAdminProduct(payload)
    }
    router.push('/admin/products')
  } catch (requestError) {
    error.value = getApiError(requestError, 'Product could not be saved.')
  } finally {
    submitting.value = false
  }
}
</script>
