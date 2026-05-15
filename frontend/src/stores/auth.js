import { defineStore } from 'pinia'

const STORAGE_KEY = 'novacart_admin_auth'

export const useAuthStore = defineStore('auth', {
  state: () => ({
    token: null,
    email: null,
    role: null,
    expiresAt: null
  }),
  getters: {
    isAuthenticated: (state) => Boolean(state.token) && !isExpired(state.expiresAt)
  },
  actions: {
    loadSession() {
      const rawSession = localStorage.getItem(STORAGE_KEY)
      if (!rawSession) return 'empty'

      try {
        const session = JSON.parse(rawSession)
        this.token = session.token
        this.email = session.email
        this.role = session.role
        this.expiresAt = Number(session.expiresAt) || null
        return this.validateSession()
      } catch {
        localStorage.removeItem(STORAGE_KEY)
        return 'empty'
      }
    },
    setSession(session) {
      const expiresAt = resolveExpiresAt(session)
      this.token = session.token
      this.email = session.email
      this.role = session.role
      this.expiresAt = expiresAt
      localStorage.setItem(STORAGE_KEY, JSON.stringify({
        token: session.token,
        email: session.email,
        role: session.role,
        expiresAt
      }))
    },
    validateSession() {
      if (!this.token) return 'empty'
      if (isExpired(this.expiresAt)) {
        this.clearSession()
        return 'expired'
      }
      return 'loaded'
    },
    clearSession() {
      this.token = null
      this.email = null
      this.role = null
      this.expiresAt = null
      localStorage.removeItem(STORAGE_KEY)
    }
  }
})

function resolveExpiresAt(session) {
  if (session.expiresAt) {
    return Number(session.expiresAt)
  }

  const expirationMinutes = Number(session.expiresInMinutes)
  if (!Number.isFinite(expirationMinutes) || expirationMinutes <= 0) {
    return null
  }

  return Date.now() + expirationMinutes * 60 * 1000
}

function isExpired(expiresAt) {
  return !expiresAt || Date.now() >= Number(expiresAt)
}
