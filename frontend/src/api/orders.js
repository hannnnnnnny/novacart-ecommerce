import apiClient, { getApiData } from './client'

export async function createOrder(payload) {
  return getApiData(await apiClient.post('/public/orders', payload))
}

export async function fetchOrder(id) {
  return getApiData(await apiClient.get(`/public/orders/${id}`))
}
