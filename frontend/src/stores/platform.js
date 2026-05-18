import { defineStore } from 'pinia'
import { createSlug, demoStores, getTemplateById, storeTemplates } from '../data/platform'

const STORAGE_KEY = 'novacart_platform_stores'
const OVERRIDE_KEY = 'novacart_platform_store_overrides'
const CURRENT_STORE_KEY = 'novacart_current_store'

export const usePlatformStore = defineStore('platform', {
  state: () => ({
    merchantStores: [],
    storeOverrides: {},
    currentStoreSlug: 'demo-fashion'
  }),
  getters: {
    stores: (state) => [
      ...demoStores.map((store) => ({
        ...store,
        ...(state.storeOverrides[store.slug] || {}),
        setup: {
          ...store.setup,
          ...(state.storeOverrides[store.slug]?.setup || {})
        }
      })),
      ...state.merchantStores
    ],
    currentStore() {
      return this.stores.find((store) => store.slug === this.currentStoreSlug) || this.stores[0]
    },
    templates: () => storeTemplates
  },
  actions: {
    loadPlatform() {
      const storedCurrent = localStorage.getItem(CURRENT_STORE_KEY)
      if (storedCurrent) this.currentStoreSlug = storedCurrent

      const rawStores = localStorage.getItem(STORAGE_KEY)
      if (rawStores) {
        try {
          const parsedStores = JSON.parse(rawStores)
          this.merchantStores = Array.isArray(parsedStores) ? parsedStores.map(normalizeStore).filter(Boolean) : []
        } catch {
          localStorage.removeItem(STORAGE_KEY)
        }
      }

      const rawOverrides = localStorage.getItem(OVERRIDE_KEY)
      if (rawOverrides) {
        try {
          const parsedOverrides = JSON.parse(rawOverrides)
          this.storeOverrides = parsedOverrides && typeof parsedOverrides === 'object' ? parsedOverrides : {}
        } catch {
          localStorage.removeItem(OVERRIDE_KEY)
        }
      }
    },
    setCurrentStore(slug) {
      const nextStore = this.stores.find((store) => store.slug === slug)
      if (!nextStore) return
      this.currentStoreSlug = nextStore.slug
      localStorage.setItem(CURRENT_STORE_KEY, nextStore.slug)
    },
    getStore(slug) {
      return this.stores.find((store) => store.slug === slug)
    },
    createStore(payload) {
      const template = getTemplateById(payload.template || 'fashion')
      const slug = uniqueSlug(payload.slug || payload.name, this.stores)
      const nextStore = {
        id: `store-${Date.now()}`,
        merchantName: payload.merchantName || payload.name || 'New Merchant',
        name: payload.name || 'New Store',
        slug,
        category: payload.category || 'Lifestyle',
        description: payload.description || 'A NovaCart merchant storefront.',
        template: template.id,
        brandColor: payload.brandColor || template.accentColor,
        logoText: payload.logoText || initials(payload.name || 'New Store'),
        currency: payload.currency || 'USD',
        shippingMessage: payload.shippingMessage || 'Free shipping on qualifying orders',
        announcement: payload.announcement || payload.shippingMessage || 'Welcome to our new NovaCart store.',
        heroTitle: payload.heroTitle || `Shop ${payload.name || 'our store'}`,
        heroText: payload.heroText || payload.description || 'Discover products from this independent merchant.',
        published: false,
        setup: {
          details: Boolean(payload.name),
          template: Boolean(payload.template),
          products: Boolean(payload.products?.length),
          shipping: Boolean(payload.shippingMessage),
          preview: false,
          publish: false
        },
        categories: Array.from(new Set((payload.products || []).map((product) => product.category).filter(Boolean))).concat(['New Arrivals']).slice(0, 8),
        products: normalizeProducts(payload.products || []),
        analytics: {
          sales: 0,
          orders: 0,
          visitors: 0,
          conversionRate: '0.0%',
          averageOrderValue: 0,
          topProducts: [],
          trafficSources: ['Direct', 'Search', 'Social']
        }
      }
      this.merchantStores.push(nextStore)
      this.setCurrentStore(nextStore.slug)
      this.persistStores()
      return nextStore
    },
    updateStore(slug, patch) {
      const nextPatch = {
        ...patch,
        slug: patch.slug ? createSlug(patch.slug) : undefined
      }
      if (!patch.slug) {
        delete nextPatch.slug
      }
      const index = this.merchantStores.findIndex((store) => store.slug === slug)
      if (index === -1) {
        if (!demoStores.some((store) => store.slug === slug)) return null
        this.storeOverrides[slug] = {
          ...(this.storeOverrides[slug] || {}),
          ...nextPatch,
          slug,
          setup: {
            ...(this.storeOverrides[slug]?.setup || {}),
            ...(nextPatch.setup || {})
          }
        }
        localStorage.setItem(OVERRIDE_KEY, JSON.stringify(this.storeOverrides))
        return this.getStore(slug)
      }
      if (nextPatch.slug && nextPatch.slug !== slug) {
        nextPatch.slug = uniqueSlug(nextPatch.slug, this.stores.filter((store) => store.slug !== slug))
        if (this.currentStoreSlug === slug) {
          this.currentStoreSlug = nextPatch.slug
          localStorage.setItem(CURRENT_STORE_KEY, nextPatch.slug)
        }
      }
      this.merchantStores[index] = {
        ...this.merchantStores[index],
        ...nextPatch,
        setup: {
          ...this.merchantStores[index].setup,
          ...nextPatch.setup
        }
      }
      this.persistStores()
      return this.merchantStores[index]
    },
    publishStore(slug) {
      return this.updateStore(slug, {
        published: true,
        setup: { preview: true, publish: true }
      })
    },
    persistStores() {
      localStorage.setItem(STORAGE_KEY, JSON.stringify(this.merchantStores))
      localStorage.setItem(OVERRIDE_KEY, JSON.stringify(this.storeOverrides))
    }
  }
})

function normalizeStore(store) {
  if (!store?.name || !store?.slug) return null
  return {
    ...store,
    slug: createSlug(store.slug),
    products: normalizeProducts(store.products || []),
    setup: {
      details: false,
      template: false,
      products: false,
      shipping: false,
      preview: false,
      publish: false,
      ...(store.setup || {})
    }
  }
}

function normalizeProducts(products) {
  return products.map((product, index) => {
    const id = Number(product.id) || Date.now() + index
    const price = Number(product.price) || 0
    const compareAtPrice = Number(product.compareAtPrice) || null
    const discountPercent = compareAtPrice
      ? Math.max(0, Math.round(((compareAtPrice - price) / compareAtPrice) * 100))
      : 0
    return {
      id,
      name: product.name || `Product ${index + 1}`,
      slug: createSlug(product.slug || product.name || `product-${index + 1}`),
      category: product.category || 'General',
      price,
      compareAtPrice,
      effectivePrice: price,
      discountPercent,
      stockQuantity: Number(product.stockQuantity) || 12,
      lowStockThreshold: Number(product.lowStockThreshold) || 5,
      imageUrl: product.imageUrl || '/demo-images/products/boutique-shirt.jpg',
      imageGallery: product.imageGallery || [product.imageUrl || '/demo-images/products/boutique-shirt.jpg'],
      badges: product.badges || (discountPercent ? ['Sale'] : ['New']),
      status: product.status || 'ACTIVE',
      description: product.description || 'A merchant-created product ready for the storefront.'
    }
  })
}

function uniqueSlug(value, stores) {
  const base = createSlug(value)
  let candidate = base
  let suffix = 2
  while (stores.some((store) => store.slug === candidate)) {
    candidate = `${base}-${suffix}`
    suffix += 1
  }
  return candidate
}

function initials(value) {
  return String(value || 'NS')
    .split(/\s+/)
    .filter(Boolean)
    .slice(0, 2)
    .map((part) => part[0]?.toUpperCase())
    .join('') || 'NS'
}
