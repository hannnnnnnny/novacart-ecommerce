import apiClient, { getApiData } from './client'

export async function createOrder(payload) {
  return getApiData(await apiClient.post('/public/orders', payload))
}

export async function fetchOrder(id) {
  return getApiData(await apiClient.get(`/public/orders/${id}`))
}

export async function lookupOrder(params) {
  return getApiData(await apiClient.get('/public/orders/lookup', { params }))
}

export async function createSupportTicket(payload) {
  return getApiData(await apiClient.post('/public/support-tickets', payload))
}

export async function createRefundRequest(payload) {
  return getApiData(await apiClient.post('/public/refunds', payload))
}
