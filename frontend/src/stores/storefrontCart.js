import { defineStore } from 'pinia'

const STORAGE_KEY = 'novacart_storefront_carts'

export const useStorefrontCartStore = defineStore('storefrontCart', {
  state: () => ({
    carts: {}
  }),
  getters: {
    itemsForStore: (state) => (storeSlug) => state.carts[storeSlug] || [],
    itemCountForStore: (state) => (storeSlug) => (state.carts[storeSlug] || []).reduce((total, item) => total + item.quantity, 0),
    subtotalForStore: (state) => (storeSlug) => (state.carts[storeSlug] || []).reduce((total, item) => total + item.price * item.quantity, 0),
    discountTotalForStore: (state) => (storeSlug) => (state.carts[storeSlug] || []).reduce((total, item) => total + (item.discountAmount || 0) * item.quantity, 0)
  },
  actions: {
    loadCarts() {
      const rawCarts = localStorage.getItem(STORAGE_KEY)
      if (!rawCarts) return
      try {
        const parsed = JSON.parse(rawCarts)
        this.carts = parsed && typeof parsed === 'object' ? parsed : {}
      } catch {
        localStorage.removeItem(STORAGE_KEY)
      }
    },
    addItem(storeSlug, product, quantity = 1, options = {}) {
      if (!storeSlug || !product || product.stockQuantity < 1) return
      if (!this.carts[storeSlug]) this.carts[storeSlug] = []
      const requestedQuantity = Math.max(1, Math.floor(Number(quantity) || 1))
      const selectedOptions = {
        size: options.size || '',
        color: options.color || ''
      }
      const itemId = cartItemId(product.id, selectedOptions)
      const existing = this.carts[storeSlug].find((item) => (item.itemId || cartItemId(item.productId, item.options || {})) === itemId)
      if (existing) {
        existing.quantity = Math.min(existing.quantity + requestedQuantity, product.stockQuantity)
        existing.stockQuantity = product.stockQuantity
      } else {
        const compareAtPrice = Number(product.compareAtPrice) || null
        const price = Number(product.effectivePrice ?? product.price) || 0
        this.carts[storeSlug].push({
          itemId,
          productId: product.id,
          name: product.name,
          price,
          compareAtPrice,
          discountAmount: compareAtPrice ? Math.max(0, compareAtPrice - price) : 0,
          discountPercent: product.discountPercent || 0,
          imageUrl: product.imageUrl,
          stockQuantity: product.stockQuantity,
          options: selectedOptions,
          quantity: Math.min(requestedQuantity, product.stockQuantity)
        })
      }
      this.persist()
    },
    updateQuantity(storeSlug, itemId, quantity) {
      const cart = this.carts[storeSlug] || []
      const item = cart.find((entry) => matchesCartItem(entry, itemId))
      if (!item) return
      const nextQuantity = Math.floor(Number(quantity) || 0)
      if (nextQuantity < 1) {
        this.removeItem(storeSlug, itemId)
        return
      }
      item.quantity = Math.min(nextQuantity, item.stockQuantity)
      this.persist()
    },
    removeItem(storeSlug, itemId) {
      this.carts[storeSlug] = (this.carts[storeSlug] || []).filter((item) => !matchesCartItem(item, itemId))
      this.persist()
    },
    clearStoreCart(storeSlug) {
      this.carts[storeSlug] = []
      this.persist()
    },
    persist() {
      localStorage.setItem(STORAGE_KEY, JSON.stringify(this.carts))
    }
  }
})

function cartItemId(productId, options = {}) {
  return [productId, options.size || '', options.color || ''].join('::')
}

function matchesCartItem(item, itemId) {
  return item.itemId === itemId || String(item.productId) === String(itemId)
}
