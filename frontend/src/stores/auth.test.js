import { afterEach, beforeEach, describe, expect, it, vi } from 'vitest'
import { createPinia, setActivePinia } from 'pinia'
import { useAuthStore } from './auth'

const STORAGE_KEY = 'novacart_admin_auth'

describe('admin auth store', () => {
  let storage

  beforeEach(() => {
    storage = new Map()
    Object.defineProperty(globalThis, 'localStorage', {
      configurable: true,
      value: {
        getItem: (key) => storage.get(key) || null,
        setItem: (key, value) => storage.set(key, value),
        removeItem: (key) => storage.delete(key)
      }
    })

    vi.useFakeTimers()
    vi.setSystemTime(new Date('2026-05-15T00:00:00Z'))
    setActivePinia(createPinia())
  })

  afterEach(() => {
    vi.useRealTimers()
  })

  it('stores login sessions with a calculated expiration time', () => {
    const authStore = useAuthStore()

    authStore.setSession({
      token: 'token-value',
      email: 'admin@novacart.local',
      role: 'ADMIN',
      expiresInMinutes: 30
    })

    const savedSession = JSON.parse(storage.get(STORAGE_KEY))
    expect(authStore.isAuthenticated).toBe(true)
    expect(savedSession.expiresAt).toBe(new Date('2026-05-15T00:30:00Z').getTime())
  })

  it('clears expired sessions loaded from storage', () => {
    storage.set(STORAGE_KEY, JSON.stringify({
      token: 'expired-token',
      email: 'admin@novacart.local',
      role: 'ADMIN',
      expiresAt: new Date('2026-05-14T23:59:00Z').getTime()
    }))

    const authStore = useAuthStore()

    expect(authStore.loadSession()).toBe('expired')
    expect(authStore.isAuthenticated).toBe(false)
    expect(storage.get(STORAGE_KEY)).toBeUndefined()
  })

  it('invalidates an active store session after expiration passes', () => {
    const authStore = useAuthStore()
    authStore.setSession({
      token: 'token-value',
      email: 'admin@novacart.local',
      role: 'ADMIN',
      expiresInMinutes: 1
    })

    vi.setSystemTime(new Date('2026-05-15T00:02:00Z'))

    expect(authStore.validateSession()).toBe('expired')
    expect(authStore.token).toBeNull()
    expect(authStore.isAuthenticated).toBe(false)
  })
})
