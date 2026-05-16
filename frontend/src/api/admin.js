import apiClient, { getApiData } from './client'

export async function loginAdmin(payload) {
  return getApiData(await apiClient.post('/admin/auth/login', payload))
}

export async function fetchDashboardMetrics() {
  return getApiData(await apiClient.get('/admin/dashboard/metrics'))
}

export async function fetchInventoryWarnings(threshold = 5) {
  return getApiData(await apiClient.get('/admin/inventory/warnings', { params: { threshold } }))
}

export async function fetchInventoryMovements() {
  return getApiData(await apiClient.get('/admin/inventory/movements'))
}

export async function fetchAdminProducts() {
  return (await fetchAdminProductPage({ size: 60 })).content
}

export async function fetchAdminProductPage(params = {}) {
  const data = getApiData(await apiClient.get('/admin/products', { params }))
  if (Array.isArray(data)) {
    return {
      content: data,
      page: 0,
      size: data.length,
      totalElements: data.length,
      totalPages: data.length ? 1 : 0,
      first: true,
      last: true,
      empty: data.length === 0
    }
  }
  return data
}

export async function fetchAdminProduct(id) {
  return getApiData(await apiClient.get(`/admin/products/${id}`))
}

export async function createAdminProduct(payload) {
  return getApiData(await apiClient.post('/admin/products', payload))
}

export async function updateAdminProduct(id, payload) {
  return getApiData(await apiClient.put(`/admin/products/${id}`, payload))
}

export async function deleteAdminProduct(id) {
  return getApiData(await apiClient.delete(`/admin/products/${id}`))
}

export async function fetchAdminCategories() {
  return getApiData(await apiClient.get('/admin/categories'))
}

export async function createAdminCategory(payload) {
  return getApiData(await apiClient.post('/admin/categories', payload))
}

export async function updateAdminCategory(id, payload) {
  return getApiData(await apiClient.put(`/admin/categories/${id}`, payload))
}

export async function deleteAdminCategory(id) {
  return getApiData(await apiClient.delete(`/admin/categories/${id}`))
}

export async function fetchAdminCollections() {
  return getApiData(await apiClient.get('/admin/collections'))
}

export async function createAdminCollection(payload) {
  return getApiData(await apiClient.post('/admin/collections', payload))
}

export async function updateAdminCollection(id, payload) {
  return getApiData(await apiClient.put(`/admin/collections/${id}`, payload))
}

export async function deleteAdminCollection(id) {
  return getApiData(await apiClient.delete(`/admin/collections/${id}`))
}

export async function fetchAdminPromotions() {
  return getApiData(await apiClient.get('/admin/promotions'))
}

export async function createAdminPromotion(payload) {
  return getApiData(await apiClient.post('/admin/promotions', payload))
}

export async function updateAdminPromotion(id, payload) {
  return getApiData(await apiClient.put(`/admin/promotions/${id}`, payload))
}

export async function deleteAdminPromotion(id) {
  return getApiData(await apiClient.delete(`/admin/promotions/${id}`))
}

export async function fetchAdminOrders() {
  return getApiData(await apiClient.get('/admin/orders'))
}

export async function fetchAdminOrder(id) {
  return getApiData(await apiClient.get(`/admin/orders/${id}`))
}

export async function updateOrderStatus(id, status) {
  return getApiData(await apiClient.patch(`/admin/orders/${id}/status`, { status }))
}

export async function fetchAdminSupportTickets() {
  return getApiData(await apiClient.get('/admin/support-tickets'))
}

export async function updateAdminSupportTicket(id, payload) {
  return getApiData(await apiClient.patch(`/admin/support-tickets/${id}`, payload))
}

export async function fetchAdminRefunds(params = {}) {
  return getApiData(await apiClient.get('/admin/refunds', { params }))
}

export async function updateAdminRefund(id, payload) {
  return getApiData(await apiClient.patch(`/admin/refunds/${id}`, payload))
}

export async function fetchAdminAnalytics() {
  return getApiData(await apiClient.get('/admin/analytics'))
}

export async function fetchAdminCustomers() {
  return getApiData(await apiClient.get('/admin/customers'))
}
