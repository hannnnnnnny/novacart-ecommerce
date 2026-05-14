<template>
  <main class="login-page">
    <form class="login-panel" @submit.prevent="submitLogin">
      <p class="eyebrow">Merchant Access</p>
      <h1>Sign in to NovaCart</h1>
      <ErrorMessage v-if="error" :message="error" />
      <label>Email<input v-model="form.email" required type="email" placeholder="admin@novacart.local" /></label>
      <label>Password<input v-model="form.password" required type="password" placeholder="Enter your password" /></label>
      <button class="primary-button" type="submit" :disabled="submitting">
        {{ submitting ? 'Signing In...' : 'Sign In' }}
      </button>
    </form>
  </main>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { loginAdmin } from '../../api/admin'
import { getApiError } from '../../api/client'
import ErrorMessage from '../../components/ErrorMessage.vue'
import { useAuthStore } from '../../stores/auth'

const route = useRoute()
const router = useRouter()
const authStore = useAuthStore()
const submitting = ref(false)
const error = ref('')
const form = reactive({ email: '', password: '' })

async function submitLogin() {
  error.value = ''
  submitting.value = true
  try {
    const session = await loginAdmin(form)
    authStore.setSession(session)
    router.push(route.query.redirect || '/admin/dashboard')
  } catch (requestError) {
    error.value = getApiError(requestError, 'Sign in failed.')
  } finally {
    submitting.value = false
  }
}
</script>
