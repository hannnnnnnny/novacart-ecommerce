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

export function getApiData(response) {
  return response.data?.data ?? response.data
}

export function getApiError(error, fallbackMessage = 'The request could not be completed.') {
  return error.response?.data?.message || error.message || fallbackMessage
}

export default apiClient
