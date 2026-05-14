import apiClient, { getApiData } from './client'

export async function fetchCategories() {
  return getApiData(await apiClient.get('/public/categories'))
}

export async function fetchProducts(categoryId) {
  const params = categoryId ? { categoryId } : {}
  return getApiData(await apiClient.get('/public/products', { params }))
}

export async function fetchProduct(id) {
  return getApiData(await apiClient.get(`/public/products/${id}`))
}
