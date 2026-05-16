<template>
  <section class="admin-page">
    <PageHeader
      eyebrow="Pricing"
      title="Promotions"
      description="Build percentage or fixed discounts for selected products, categories, collections, seasons, or tags."
    />
    <LoadingState v-if="loading" message="Loading promotions..." />
    <ErrorMessage v-else-if="error" :message="error" />
    <div v-else class="admin-split-layout">
      <form class="admin-form" @submit.prevent="savePromotion">
        <h2>{{ editingId ? 'Edit Promotion' : 'New Promotion' }}</h2>
        <label>Name<input v-model.trim="form.name" required maxlength="140" /></label>
        <label class="wide-field">Description<textarea v-model.trim="form.description" rows="3" maxlength="800"></textarea></label>
        <label>Discount Type
          <select v-model="form.discountType">
            <option value="PERCENTAGE">Percentage</option>
            <option value="FIXED_AMOUNT">Fixed amount</option>
          </select>
        </label>
        <label>Discount Value<input v-model.number="form.discountValue" required min="0.01" step="0.01" type="number" /></label>
        <label>Target Type
          <select v-model="form.targetType">
            <option value="SELECTED_PRODUCTS">Selected products</option>
            <option value="CATEGORY">Category</option>
            <option value="COLLECTION">Collection</option>
            <option value="SEASON">Season</option>
            <option value="TAGS">Tags</option>
          </select>
        </label>
        <label class="wide-field">Target Values<textarea v-model.trim="form.targetValuesText" rows="3" placeholder="sale, last-season or collection slug"></textarea></label>
        <label>Start Date<input v-model="form.startDate" type="date" /></label>
        <label>End Date<input v-model="form.endDate" type="date" /></label>
        <label class="checkbox-field"><input v-model="form.active" type="checkbox" /> Active</label>
        <div class="form-actions">
          <button class="primary-button" type="submit" :disabled="saving || !form.name || !form.discountValue">{{ saving ? 'Saving...' : 'Save Promotion' }}</button>
          <button v-if="editingId" class="secondary-button" type="button" @click="resetForm">Cancel Edit</button>
        </div>
      </form>
      <div class="admin-table-wrap">
        <table class="admin-table compact-table">
          <thead><tr><th>Name</th><th>Discount</th><th>Target</th><th>Active</th><th>Actions</th></tr></thead>
          <tbody>
            <tr v-for="promotion in promotions" :key="promotion.id">
              <td><strong>{{ promotion.name }}</strong><span>{{ promotion.description }}</span></td>
              <td>{{ promotion.discountType === 'PERCENTAGE' ? `${promotion.discountValue}%` : formatCurrency(promotion.discountValue) }}</td>
              <td>{{ formatStatus(promotion.targetType) }}: {{ promotion.targetValues.join(', ') }}</td>
              <td><StatusBadge :value="promotion.active ? 'ACTIVE' : 'INACTIVE'" /></td>
              <td>
                <div class="table-actions">
                  <button class="text-button" type="button" @click="editPromotion(promotion)">Edit</button>
                  <button class="text-button danger" type="button" @click="removePromotion(promotion)">Delete</button>
                </div>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </section>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import {
  createAdminPromotion,
  deleteAdminPromotion,
  fetchAdminPromotions,
  updateAdminPromotion
} from '../../api/admin'
import { getApiError } from '../../api/client'
import ErrorMessage from '../../components/ErrorMessage.vue'
import LoadingState from '../../components/LoadingState.vue'
import PageHeader from '../../components/PageHeader.vue'
import StatusBadge from '../../components/StatusBadge.vue'
import { formatCurrency, formatStatus } from '../../utils/format'

const loading = ref(true)
const saving = ref(false)
const error = ref('')
const promotions = ref([])
const editingId = ref(null)
const form = reactive(emptyForm())

onMounted(loadPromotions)

async function loadPromotions() {
  loading.value = true
  error.value = ''
  try {
    promotions.value = await fetchAdminPromotions()
  } catch (requestError) {
    error.value = getApiError(requestError, 'Promotions could not be loaded.')
  } finally {
    loading.value = false
  }
}

async function savePromotion() {
  saving.value = true
  error.value = ''
  try {
    const payload = {
      ...form,
      discountValue: Number(form.discountValue),
      startDate: form.startDate || null,
      endDate: form.endDate || null,
      targetValues: splitValues(form.targetValuesText)
    }
    delete payload.targetValuesText
    if (editingId.value) {
      await updateAdminPromotion(editingId.value, payload)
    } else {
      await createAdminPromotion(payload)
    }
    resetForm()
    await loadPromotions()
  } catch (requestError) {
    error.value = getApiError(requestError, 'Promotion could not be saved.')
  } finally {
    saving.value = false
  }
}

function editPromotion(promotion) {
  editingId.value = promotion.id
  Object.assign(form, {
    name: promotion.name,
    description: promotion.description || '',
    discountType: promotion.discountType,
    discountValue: Number(promotion.discountValue),
    targetType: promotion.targetType,
    targetValuesText: promotion.targetValues.join(', '),
    startDate: promotion.startDate || '',
    endDate: promotion.endDate || '',
    active: Boolean(promotion.active)
  })
}

async function removePromotion(promotion) {
  if (!window.confirm(`Delete promotion ${promotion.name}?`)) return
  try {
    await deleteAdminPromotion(promotion.id)
    promotions.value = promotions.value.filter((entry) => entry.id !== promotion.id)
  } catch (requestError) {
    error.value = getApiError(requestError, 'Promotion could not be deleted.')
  }
}

function resetForm() {
  editingId.value = null
  Object.assign(form, emptyForm())
}

function splitValues(value) {
  return String(value || '')
    .split(',')
    .map((entry) => entry.trim())
    .filter(Boolean)
}

function emptyForm() {
  return {
    name: '',
    description: '',
    discountType: 'PERCENTAGE',
    discountValue: 10,
    targetType: 'TAGS',
    targetValuesText: 'sale',
    startDate: '',
    endDate: '',
    active: true
  }
}
</script>
