<template>
  <section class="admin-page">
    <div class="admin-page-header">
      <h1>Products</h1>
      <RouterLink class="primary-button" to="/admin/products/new">New Product</RouterLink>
    </div>
    <LoadingState v-if="loading" message="Loading products..." />
    <ErrorMessage v-else-if="error" :message="error" />
    <EmptyState
      v-else-if="!products.length"
      title="No products yet"
      message="Create the first product to publish it in the storefront catalog."
    />
    <div v-else class="admin-table-wrap">
      <table class="admin-table">
        <thead>
          <tr>
            <th>Name</th>
            <th>Category</th>
            <th>Price</th>
            <th>Stock</th>
            <th>Status</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="product in products" :key="product.id">
            <td>
              <strong>{{ product.name }}</strong>
              <span>{{ product.slug }}</span>
            </td>
            <td>{{ product.category?.name }}</td>
            <td>{{ formatCurrency(product.price) }}</td>
            <td>{{ product.stockQuantity }}</td>
            <td>
              <span class="status-pill" :class="{ muted: !product.active }">
                {{ product.active ? 'Active' : 'Inactive' }}
              </span>
            </td>
            <td>
              <div class="table-actions">
                <RouterLink class="text-link" :to="`/admin/products/${product.id}/edit`">Edit</RouterLink>
                <button class="text-button danger" type="button" @click="removeProduct(product)">Delete</button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </section>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { deleteAdminProduct, fetchAdminProducts } from '../../api/admin'
import { getApiError } from '../../api/client'
import EmptyState from '../../components/EmptyState.vue'
import ErrorMessage from '../../components/ErrorMessage.vue'
import LoadingState from '../../components/LoadingState.vue'
import { formatCurrency } from '../../utils/format'

const loading = ref(true)
const error = ref('')
const products = ref([])

onMounted(loadProducts)

async function loadProducts() {
  loading.value = true
  error.value = ''
  try {
    products.value = await fetchAdminProducts()
  } catch (requestError) {
    error.value = getApiError(requestError, 'Products could not be loaded.')
  } finally {
    loading.value = false
  }
}

async function removeProduct(product) {
  const confirmed = window.confirm(`Delete ${product.name}? This action cannot be undone.`)
  if (!confirmed) return

  try {
    await deleteAdminProduct(product.id)
    products.value = products.value.filter((entry) => entry.id !== product.id)
  } catch (requestError) {
    error.value = getApiError(requestError, 'Product could not be deleted.')
  }
}
</script>
