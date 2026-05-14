import axios from 'axios'
import { useAuthStore } from '../stores/auth'

const apiClient = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api',
  headers: {
    'Content-Type': 'application/json'
  }
})

apiClient.interceptors.request.use((config) => {
  const authStore = useAuthStore()
  if (authStore.token) {
    config.headers.Authorization = `Bearer ${authStore.token}`
  }
  return config
})

apiClient.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      const authStore = useAuthStore()
      authStore.clearSession()
    }
    return Promise.reject(error)
  }
)

export function getApiData(response) {
  return response.data?.data ?? response.data
}

export function getApiError(error, fallbackMessage = 'The request could not be completed.') {
  const fieldErrors = error.response?.data?.errors
  if (Array.isArray(fieldErrors) && fieldErrors.length) {
    return fieldErrors.map((entry) => entry.message).join(' ')
  }

  return error.response?.data?.message || error.message || fallbackMessage
}

export default apiClient
