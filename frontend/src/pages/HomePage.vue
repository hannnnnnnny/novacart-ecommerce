<template>
  <div>
    <section class="hero-section premium-hero-section">
      <div class="hero-copy">
        <p class="eyebrow">NovaCart Fashion</p>
        <h1>Fashion, movement, and everyday detail.</h1>
        <p>Curated clothing, bags, jewelry, shoes, sportswear, and seasonal edits with a shopping flow built around real sizes, colors, discounts, delivery, refunds, and support.</p>
        <div class="hero-actions">
          <RouterLink class="primary-button" :to="{ name: 'products', query: { tag: 'new-arrival', sort: 'newest' } }">Shop New Arrivals</RouterLink>
          <RouterLink class="secondary-button hero-secondary" :to="{ name: 'products', query: { sale: 'true', sort: 'discount' } }">Explore Sale</RouterLink>
        </div>
        <div class="hero-proof-row" aria-label="Fashion storefront highlights">
          <span>Seasonal collections</span>
          <span>Variant-aware checkout</span>
          <span>Refund and support flow</span>
        </div>
      </div>
      <div class="hero-editorial-card hero-lookbook-card" aria-label="Featured seasonal campaign">
        <span class="campaign-label">Spring Edit</span>
        <h2>Soft tailoring, sculptural bags, and active layers.</h2>
        <p>Shop a campaign mix built around weekday polish, evening detail, and movement-ready weekend pieces.</p>
        <div class="hero-lookbook-grid" aria-label="Visual fashion departments">
          <RouterLink :to="{ name: 'products', query: { category: 1 } }">
            <img src="/catalog/women.svg" alt="Tailored womenswear preview" />
            <span>Clothing</span>
          </RouterLink>
          <RouterLink :to="{ name: 'products', query: { category: 3 } }">
            <img src="/catalog/bags.svg" alt="Structured bag preview" />
            <span>Bags</span>
          </RouterLink>
          <RouterLink :to="{ name: 'products', query: { category: 6 } }">
            <img src="/catalog/sportswear.svg" alt="Activewear preview" />
            <span>Activewear</span>
          </RouterLink>
        </div>
        <RouterLink class="secondary-button hero-secondary" :to="{ name: 'products', query: { search: 'spring', sort: 'newest' } }">View Campaign</RouterLink>
        <div class="hero-stats">
          <article>
            <strong>{{ products.length || '60' }}</strong>
            <span>Fashion products</span>
          </article>
          <article>
            <strong>{{ categories.length || '10' }}</strong>
            <span>Retail categories</span>
          </article>
          <article>
            <strong>{{ collections.length || '6' }}</strong>
            <span>Seasonal collections</span>
          </article>
        </div>
      </div>
    </section>

    <section class="page-section category-section">
      <SectionHeader
        eyebrow="Wardrobe departments"
        title="Browse by fashion focus"
        description="Move quickly between clothing, accessories, footwear, sportswear, sale, and seasonal campaign edits."
      />
      <LoadingState v-if="loading" message="Loading categories..." />
      <ErrorMessage v-else-if="error" :message="error" />
      <div v-else class="category-highlight-grid">
        <RouterLink
          v-for="category in categories"
          :key="category.id"
          class="category-highlight fashion-category-card"
          :style="{ '--category-image': `url(${categoryImage(category.name)})` }"
          :to="{ name: 'products', query: { category: category.id } }"
        >
          <span>{{ category.name }}</span>
          <small>{{ category.description }}</small>
        </RouterLink>
      </div>
    </section>

    <section class="page-section featured-section">
      <SectionHeader
        eyebrow="Featured collections"
        title="Campaigns with a clear point of view"
        description="Featured collections help shoppers move from inspiration to products without losing context."
      />
      <LoadingState v-if="loading" message="Loading featured products..." />
      <ErrorMessage v-else-if="error" :message="error" />
      <div v-else class="campaign-grid">
        <RouterLink
          v-for="collection in featuredCollections"
          :key="collection.id"
          class="campaign-card visual-campaign-card"
          :style="{ '--campaign-image': `url(${collection.displayImageUrl || collection.heroImageUrl || '/catalog/seasonal.svg'})` }"
          :to="{ name: 'products', query: { collectionId: collection.id } }"
        >
          <strong>{{ collection.name }}</strong>
          <span>{{ collection.description }}</span>
        </RouterLink>
      </div>
      <div v-if="!loading && !error" class="product-grid featured-product-row">
        <ProductCard v-for="product in featuredProducts" :key="product.id" :product="product" />
      </div>
    </section>

    <section v-if="newArrivalProducts.length" class="page-section home-product-section">
      <SectionHeader
        eyebrow="New arrivals"
        title="Fresh wardrobe updates"
        description="Recently seeded fashion products with size, color, material, and collection metadata ready for checkout."
      >
        <template #actions>
          <RouterLink class="text-link" :to="{ name: 'products', query: { tag: 'new-arrival', sort: 'newest' } }">View All New Arrivals</RouterLink>
        </template>
      </SectionHeader>
      <div class="product-grid">
        <ProductCard v-for="product in newArrivalProducts" :key="product.id" :product="product" />
      </div>
    </section>

    <section v-if="bestSellerProducts.length" class="page-section home-product-section">
      <SectionHeader
        eyebrow="Best sellers"
        title="Pieces with stronger merchandising signals"
        description="Featured products show how NovaCart highlights popular apparel, jewelry, bags, footwear, and sportswear."
      >
        <template #actions>
          <RouterLink class="text-link" :to="{ name: 'products', query: { sort: 'best-selling' } }">Shop Best Sellers</RouterLink>
        </template>
      </SectionHeader>
      <div class="product-grid">
        <ProductCard v-for="product in bestSellerProducts" :key="product.id" :product="product" />
      </div>
    </section>

    <section class="page-section campaign-section">
      <SectionHeader
        eyebrow="Seasonal campaign"
        title="Editorial shopping paths"
        description="Campaign links route to real filters for seasonal looks, activewear, evening details, and sale markdowns."
      />
      <div class="campaign-grid">
        <RouterLink
          v-for="tile in campaignTiles"
          :key="tile.title"
          class="campaign-card"
          :to="{ name: 'products', query: tile.query }"
        >
          <small>{{ tile.eyebrow }}</small>
          <strong>{{ tile.title }}</strong>
          <span>{{ tile.description }}</span>
        </RouterLink>
      </div>
    </section>

    <section v-if="saleProducts.length" class="page-section sale-band">
      <div>
        <p class="eyebrow">End of Season Sale</p>
        <h2>Marked-down fashion with real discount pricing.</h2>
        <p>Sale cards show original price, discounted price, and badge logic from the backend promotion and compare-at data.</p>
        <RouterLink class="primary-button" :to="{ name: 'products', query: { sale: 'true', sort: 'discount' } }">Shop Sale Campaign</RouterLink>
      </div>
      <div class="sale-product-list">
        <RouterLink v-for="product in saleProducts" :key="product.id" :to="`/products/${product.id}`">
          <span>{{ product.category?.name }}</span>
          <strong>{{ product.name }}</strong>
          <small>{{ formatCurrency(product.effectivePrice ?? product.price) }}</small>
        </RouterLink>
      </div>
    </section>

    <section class="page-section value-section">
      <div class="value-grid">
        <article v-for="item in valueCards" :key="item.title" class="value-card">
          <component :is="item.icon" aria-hidden="true" />
          <h2>{{ item.title }}</h2>
          <p>{{ item.description }}</p>
        </article>
      </div>
    </section>
  </div>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import { ShieldCheck, Sparkles, Truck } from 'lucide-vue-next'
import { fetchCategories, fetchFeaturedCollections, fetchProducts } from '../api/catalog'
import { getApiError } from '../api/client'
import ErrorMessage from '../components/ErrorMessage.vue'
import LoadingState from '../components/LoadingState.vue'
import ProductCard from '../components/ProductCard.vue'
import SectionHeader from '../components/SectionHeader.vue'
import { formatCurrency } from '../utils/format'

const loading = ref(true)
const error = ref('')
const products = ref([])
const categories = ref([])
const collections = ref([])
const featuredProducts = computed(() => products.value.slice(0, 3))
const featuredCollections = computed(() => collections.value.slice(0, 3))
const newArrivalProducts = computed(() => products.value.filter((product) => product.tags?.includes('new-arrival')).slice(0, 4))
const bestSellerProducts = computed(() => products.value.filter((product) => product.featured || product.tags?.includes('bestseller')).slice(0, 4))
const saleProducts = computed(() => products.value.filter((product) => product.discountPercent || product.tags?.includes('sale')).slice(0, 4))
const campaignTiles = [
  {
    eyebrow: 'Tailoring',
    title: 'Workwear Capsule',
    description: 'Blazers, trousers, knitwear, and structured accessories for weekday polish.',
    query: { search: 'workwear', sort: 'newest' }
  },
  {
    eyebrow: 'Movement',
    title: 'Active Weekend',
    description: 'Performance layers, sneakers, equipment, and hands-free bags for travel and training.',
    query: { tag: 'active-weekend', sort: 'best-selling' }
  },
  {
    eyebrow: 'Occasion',
    title: 'Evening Details',
    description: 'Jewelry, satin, clutches, watches, and finishers with sharper event styling.',
    query: { search: 'evening', sort: 'price-high' }
  }
]
const valueCards = [
  {
    title: 'Variant-Aware Checkout',
    description: 'Size, color, stock checks, demo payment, and refund acknowledgement stay clear from cart to confirmation.',
    icon: Truck
  },
  {
    title: 'Curated Fashion Catalog',
    description: 'Seasonal collections, sale edits, private labels, and visual product cards keep browsing focused.',
    icon: Sparkles
  },
  {
    title: 'Merchant Operations',
    description: 'Protected admin screens manage products, collections, promotions, orders, refunds, support, and inventory.',
    icon: ShieldCheck
  }
]
const categoryImages = {
  Women: '/catalog/women.svg',
  Men: '/catalog/men.svg',
  Bags: '/catalog/bags.svg',
  Jewelry: '/catalog/jewelry.svg',
  Shoes: '/catalog/shoes.svg',
  Sportswear: '/catalog/sportswear.svg',
  Accessories: '/catalog/accessories.svg',
  'New Arrivals': '/catalog/new-arrivals.svg',
  Sale: '/catalog/sale.svg',
  'Seasonal Collection': '/catalog/seasonal.svg'
}

onMounted(async () => {
  try {
    const [categoryData, collectionData, productData] = await Promise.all([fetchCategories(), fetchFeaturedCollections(), fetchProducts()])
    categories.value = categoryData
    collections.value = collectionData
    products.value = productData
  } catch (requestError) {
    error.value = getApiError(requestError, 'Storefront content could not be loaded.')
  } finally {
    loading.value = false
  }
})

function categoryImage(name) {
  return categoryImages[name] || '/catalog/seasonal.svg'
}
</script>
