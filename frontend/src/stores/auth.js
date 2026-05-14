import { defineStore } from 'pinia'

const STORAGE_KEY = 'novacart_admin_auth'

export const useAuthStore = defineStore('auth', {
  state: () => ({
    token: null,
    email: null,
    role: null
  }),
  getters: {
    isAuthenticated: (state) => Boolean(state.token)
  },
  actions: {
    loadSession() {
      const rawSession = localStorage.getItem(STORAGE_KEY)
      if (!rawSession) return

      try {
        const session = JSON.parse(rawSession)
        this.token = session.token
        this.email = session.email
        this.role = session.role
      } catch {
        localStorage.removeItem(STORAGE_KEY)
      }
    },
    setSession(session) {
      this.token = session.token
      this.email = session.email
      this.role = session.role
      localStorage.setItem(STORAGE_KEY, JSON.stringify(session))
    },
    clearSession() {
      this.token = null
      this.email = null
      this.role = null
      localStorage.removeItem(STORAGE_KEY)
    }
  }
})
