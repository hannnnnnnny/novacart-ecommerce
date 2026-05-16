import { defineStore } from 'pinia'

const STORAGE_KEY = 'novacart_cart'
const MIN_QUANTITY = 1

export const useCartStore = defineStore('cart', {
  state: () => ({
    items: []
  }),
  getters: {
    itemCount: (state) => state.items.reduce((total, item) => total + item.quantity, 0),
    subtotal: (state) => state.items.reduce((total, item) => total + Number(item.price) * item.quantity, 0),
    discountTotal: (state) => state.items.reduce((total, item) => total + Number(item.discountAmount || 0) * item.quantity, 0)
  },
  actions: {
    loadCart() {
      const rawCart = localStorage.getItem(STORAGE_KEY)
      if (!rawCart) return

      try {
        const parsedCart = JSON.parse(rawCart)
        this.items = Array.isArray(parsedCart)
          ? parsedCart.map(normalizeStoredItem).filter(Boolean)
          : []
        this.persist()
      } catch {
        localStorage.removeItem(STORAGE_KEY)
      }
    },
    addItem(product, quantity = 1, options = {}) {
      const stockQuantity = normalizeStock(product?.stockQuantity)
      if (!product || stockQuantity < MIN_QUANTITY) return

      const selectedSize = clean(options.selectedSize) || product.sizes?.[0] || ''
      const selectedColor = clean(options.selectedColor) || product.colors?.[0] || ''
      const requestedQuantity = Math.min(normalizeQuantity(quantity), stockQuantity)
      const lineKey = itemKey(product.id, selectedSize, selectedColor)

      const existingItem = this.items.find((item) => item.lineKey === lineKey)
      if (existingItem) {
        existingItem.name = product.name
        existingItem.price = normalizePrice(product.effectivePrice ?? product.price)
        existingItem.originalPrice = normalizePrice(product.price)
        existingItem.compareAtPrice = normalizeNullablePrice(product.compareAtPrice)
        existingItem.discountAmount = normalizePrice(product.discountAmount)
        existingItem.discountPercent = product.discountPercent || null
        existingItem.imageUrl = product.imageUrl
        existingItem.stockQuantity = stockQuantity
        existingItem.quantity = Math.min(existingItem.quantity + requestedQuantity, stockQuantity)
      } else {
        this.items.push({
          lineKey,
          productId: product.id,
          name: product.name,
          price: normalizePrice(product.effectivePrice ?? product.price),
          originalPrice: normalizePrice(product.price),
          compareAtPrice: normalizeNullablePrice(product.compareAtPrice),
          discountAmount: normalizePrice(product.discountAmount),
          discountPercent: product.discountPercent || null,
          selectedSize,
          selectedColor,
          imageUrl: product.imageUrl,
          stockQuantity,
          quantity: requestedQuantity
        })
      }
      this.persist()
    },
    updateQuantity(lineKeyOrProductId, quantity) {
      if (!Number.isFinite(quantity) || quantity < 1) {
        this.removeItem(lineKeyOrProductId)
        return
      }

      const item = this.findItem(lineKeyOrProductId)
      if (item) {
        item.quantity = Math.min(normalizeQuantity(quantity), item.stockQuantity)
        this.persist()
      }
    },
    removeItem(lineKeyOrProductId) {
      const item = this.findItem(lineKeyOrProductId)
      if (!item) return
      this.items = this.items.filter((entry) => entry.lineKey !== item.lineKey)
      this.persist()
    },
    findItem(lineKeyOrProductId) {
      return this.items.find((entry) => entry.lineKey === lineKeyOrProductId)
        || this.items.find((entry) => entry.productId === lineKeyOrProductId)
    },
    clearCart() {
      this.items = []
      this.persist()
    },
    persist() {
      localStorage.setItem(STORAGE_KEY, JSON.stringify(this.items))
    }
  }
})

function normalizeStoredItem(item) {
  const stockQuantity = normalizeStock(item?.stockQuantity)
  const productId = Number(item?.productId)
  const price = Number(item?.price)

  if (!productId || !item?.name || stockQuantity < MIN_QUANTITY || !Number.isFinite(price) || price < 0) {
    return null
  }

  const selectedSize = clean(item.selectedSize)
  const selectedColor = clean(item.selectedColor)
  return {
    lineKey: item.lineKey || itemKey(productId, selectedSize, selectedColor),
    productId,
    name: String(item.name),
    price,
    originalPrice: normalizePrice(item.originalPrice ?? item.price),
    compareAtPrice: normalizeNullablePrice(item.compareAtPrice),
    discountAmount: normalizePrice(item.discountAmount),
    discountPercent: item.discountPercent || null,
    selectedSize,
    selectedColor,
    imageUrl: item.imageUrl || '',
    stockQuantity,
    quantity: Math.min(normalizeQuantity(item.quantity), stockQuantity)
  }
}

function itemKey(productId, selectedSize, selectedColor) {
  return [productId, clean(selectedSize), clean(selectedColor)].join('|')
}

function clean(value) {
  return value ? String(value).trim() : ''
}

function normalizeQuantity(value) {
  const quantity = Number(value)
  if (!Number.isFinite(quantity)) return MIN_QUANTITY
  return Math.max(MIN_QUANTITY, Math.floor(quantity))
}

function normalizeStock(value) {
  const stock = Number(value)
  if (!Number.isFinite(stock)) return 0
  return Math.max(0, Math.floor(stock))
}

function normalizePrice(value) {
  const price = Number(value)
  if (!Number.isFinite(price)) return 0
  return Math.max(0, price)
}

function normalizeNullablePrice(value) {
  const price = Number(value)
  if (!Number.isFinite(price) || price <= 0) return null
  return price
}
