import apiClient, { getApiData } from './client'

export async function fetchPlatformStores() {
  return getApiData(await apiClient.get('/public/stores'))
}

export async function fetchPlatformStore(slug) {
  return getApiData(await apiClient.get(`/public/stores/${slug}`))
}
