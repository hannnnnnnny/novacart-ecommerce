<template>
  <section class="admin-page">
    <div class="admin-page-header">
      <h1>Categories</h1>
      <button class="secondary-button" type="button" @click="resetForm">New Category</button>
    </div>
    <div class="split-admin-layout">
      <form class="admin-form single-column" @submit.prevent="submitCategory">
        <h2>{{ editingId ? 'Edit Category' : 'New Category' }}</h2>
        <ErrorMessage v-if="formError" :message="formError" />
        <label>
          Name
          <input v-model.trim="form.name" required maxlength="120" placeholder="Category name" />
        </label>
        <label>
          Slug
          <input v-model.trim="form.slug" maxlength="140" placeholder="optional-category-slug" />
        </label>
        <label>
          Description
          <textarea v-model.trim="form.description" maxlength="500" rows="4" placeholder="Describe the category."></textarea>
        </label>
        <label class="checkbox-field">
          <input v-model="form.active" type="checkbox" />
          Active in storefront
        </label>
        <button class="primary-button" type="submit" :disabled="submitting">
          {{ submitting ? 'Saving Category...' : 'Save Category' }}
        </button>
      </form>

      <div>
        <LoadingState v-if="loading" message="Loading categories..." />
        <ErrorMessage v-else-if="listError" :message="listError" />
        <EmptyState
          v-else-if="!categories.length"
          title="No categories yet"
          message="Create categories to organize storefront products."
        />
        <div v-else class="admin-table-wrap">
          <table class="admin-table">
            <thead>
              <tr>
                <th>Name</th>
                <th>Slug</th>
                <th>Status</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="category in categories" :key="category.id">
                <td>
                  <strong>{{ category.name }}</strong>
                  <span>{{ category.description || 'No description provided.' }}</span>
                </td>
                <td>{{ category.slug }}</td>
                <td>
                  <span class="status-pill" :class="{ muted: !category.active }">
                    {{ category.active ? 'Active' : 'Inactive' }}
                  </span>
                </td>
                <td>
                  <div class="table-actions">
                    <button class="text-button" type="button" @click="editCategory(category)">Edit</button>
                    <button class="text-button danger" type="button" @click="removeCategory(category)">Delete</button>
                  </div>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  </section>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import {
  createAdminCategory,
  deleteAdminCategory,
  fetchAdminCategories,
  updateAdminCategory
} from '../../api/admin'
import { getApiError } from '../../api/client'
import EmptyState from '../../components/EmptyState.vue'
import ErrorMessage from '../../components/ErrorMessage.vue'
import LoadingState from '../../components/LoadingState.vue'

const categories = ref([])
const loading = ref(true)
const submitting = ref(false)
const listError = ref('')
const formError = ref('')
const editingId = ref(null)
const form = reactive({
  name: '',
  slug: '',
  description: '',
  active: true
})

onMounted(loadCategories)

async function loadCategories() {
  loading.value = true
  listError.value = ''
  try {
    categories.value = await fetchAdminCategories()
  } catch (requestError) {
    listError.value = getApiError(requestError, 'Categories could not be loaded.')
  } finally {
    loading.value = false
  }
}

function editCategory(category) {
  editingId.value = category.id
  form.name = category.name
  form.slug = category.slug
  form.description = category.description || ''
  form.active = category.active
  formError.value = ''
}

function resetForm() {
  editingId.value = null
  form.name = ''
  form.slug = ''
  form.description = ''
  form.active = true
  formError.value = ''
}

async function submitCategory() {
  formError.value = ''
  submitting.value = true
  const payload = {
    ...form,
    slug: form.slug || null,
    description: form.description || null
  }

  try {
    if (editingId.value) {
      await updateAdminCategory(editingId.value, payload)
    } else {
      await createAdminCategory(payload)
    }
    resetForm()
    await loadCategories()
  } catch (requestError) {
    formError.value = getApiError(requestError, 'Category could not be saved.')
  } finally {
    submitting.value = false
  }
}

async function removeCategory(category) {
  const confirmed = window.confirm(`Delete ${category.name}? Products assigned to this category will prevent deletion.`)
  if (!confirmed) return

  try {
    await deleteAdminCategory(category.id)
    categories.value = categories.value.filter((entry) => entry.id !== category.id)
    if (editingId.value === category.id) {
      resetForm()
    }
  } catch (requestError) {
    listError.value = getApiError(requestError, 'Category could not be deleted.')
  }
}
</script>
