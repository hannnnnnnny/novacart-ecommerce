export const storeTemplates = [
  {
    id: 'fashion',
    name: 'Fashion Boutique',
    description: 'Editorial product storytelling for apparel, bags, jewelry, footwear, and seasonal edits.',
    bestFor: 'Fashion labels, thrift boutiques, accessory shops',
    previewImage: '/catalog/women.svg',
    accentColor: '#7c3f47',
    fontStyle: 'Editorial serif'
  },
  {
    id: 'thrift',
    name: 'Thrift Classic',
    description: 'A warm discovery-led layout for curated vintage, rare finds, and mixed lifestyle goods.',
    bestFor: 'Secondhand stores, charity shops, vintage sellers',
    previewImage: '/catalog/seasonal.svg',
    accentColor: '#8a5a35',
    fontStyle: 'Classic sans'
  },
  {
    id: 'sports',
    name: 'Sports Gear',
    description: 'Fast shopping paths for activewear, equipment, accessories, and club-ready collections.',
    bestFor: 'Sports retailers, active lifestyle brands',
    previewImage: '/catalog/sportswear.svg',
    accentColor: '#245c7a',
    fontStyle: 'Modern condensed'
  },
  {
    id: 'home',
    name: 'Home Living',
    description: 'Quiet merchandising for home goods, gifts, daily essentials, and lifestyle collections.',
    bestFor: 'Homeware, lifestyle, gift stores',
    previewImage: '/catalog/accessories.svg',
    accentColor: '#6f614f',
    fontStyle: 'Warm minimal'
  },
  {
    id: 'minimal',
    name: 'Minimal Modern',
    description: 'A clean catalog-first template for small merchants that need speed, clarity, and trust.',
    bestFor: 'New stores, digital-first boutiques',
    previewImage: '/catalog/new-arrivals.svg',
    accentColor: '#30343b',
    fontStyle: 'System clean'
  }
]

export const platformFeatures = [
  ['Store builder', 'Create a branded storefront with templates, theme settings, and live previews.'],
  ['Product management', 'Add products, variants, images, categories, stock, and sale pricing from one workspace.'],
  ['Order management', 'Review payments, fulfillment states, customer details, and refund context.'],
  ['Inventory tracking', 'Watch low stock, adjust counts, and keep checkout quantities honest.'],
  ['Promotions and discounts', 'Create product, category, collection, season, or tag-based markdowns.'],
  ['Analytics dashboard', 'Track sales, orders, conversion signals, top products, and customer regions.'],
  ['Customer support tools', 'Manage support tickets, refund requests, and internal merchant notes.']
]

export const workflowSteps = [
  ['Create account', 'Start a merchant workspace for your brand.'],
  ['Pick a template', 'Choose a store design that fits your catalog.'],
  ['Add products', 'Load your first products, pricing, and inventory.'],
  ['Publish store', 'Preview the customer storefront before going live.'],
  ['Track performance', 'Use analytics, promotions, and care tools to improve.']
]

export const pricingPlans = [
  {
    name: 'Starter',
    price: '$0 demo',
    description: 'For portfolio demos and first store setup.',
    features: ['One store workspace', 'Template preview', 'Products and orders', 'Demo checkout']
  },
  {
    name: 'Growth',
    price: '$29 demo',
    description: 'For merchants ready to run campaigns.',
    features: ['Multiple templates', 'Promotions', 'Inventory alerts', 'Support queue']
  },
  {
    name: 'Pro',
    price: '$79 demo',
    description: 'For teams that need deeper operations.',
    features: ['Advanced analytics', 'Multiple staff seats', 'Theme editor', 'Priority roadmap features']
  }
]

export const demoStores = [
  {
    id: 'store-fashion',
    merchantName: 'Avery Studio',
    name: 'Avery Studio',
    slug: 'demo-fashion',
    category: 'Fashion',
    description: 'An independent fashion boutique for quiet tailoring, bags, jewelry, shoes, and seasonal wardrobe pieces.',
    template: 'fashion',
    brandColor: '#7c3f47',
    logoText: 'AS',
    currency: 'USD',
    shippingMessage: 'Free shipping on orders over $75',
    announcement: 'Spring edit now live. Free shipping on orders over $75.',
    heroTitle: 'Classic, elegant, uniquely yours',
    heroText: 'Shop timeless fashion and one-of-a-kind wardrobe finds from Avery Studio.',
    published: true,
    setup: {
      details: true,
      template: true,
      products: true,
      shipping: true,
      preview: true,
      publish: true
    },
    categories: ['New Arrivals', 'Women', 'Bags', 'Jewelry', 'Shoes', 'Sale'],
    products: [
      product(1001, 'Ivory Collarless Blazer', 'women', 'Women', 128, 168, 14, '/catalog/women.svg', ['New', 'Tailoring'], 'Soft structured blazer with a clean neckline and relaxed weekday polish.'),
      product(1002, 'Sculpted Day Bag', 'sculpted-day-bag', 'Bags', 96, null, 8, '/catalog/bags.svg', ['Best Seller'], 'A compact structured bag with a top handle and removable crossbody strap.'),
      product(1003, 'Pearl Drop Earring Set', 'pearl-drop-earring-set', 'Jewelry', 42, 58, 5, '/catalog/jewelry.svg', ['Sale', 'Low Stock'], 'Lightweight pearl drops for everyday styling and evening detail.'),
      product(1004, 'Sand Knit Midi Dress', 'sand-knit-midi-dress', 'Women', 88, null, 18, '/catalog/seasonal.svg', ['New'], 'A soft knit midi dress with easy drape and a minimal silhouette.'),
      product(1005, 'Low Profile Leather Sneaker', 'low-profile-leather-sneaker', 'Shoes', 112, 140, 11, '/catalog/shoes.svg', ['Sale'], 'Clean leather sneakers with a cushioned footbed for all-day wear.'),
      product(1006, 'Black Satin Evening Clutch', 'black-satin-evening-clutch', 'Bags', 64, null, 4, '/catalog/bags.svg', ['Low Stock'], 'A slim satin clutch with a quiet shine and interior card pocket.')
    ],
    analytics: {
      sales: 18420,
      orders: 214,
      visitors: 5620,
      conversionRate: '3.8%',
      averageOrderValue: 86.07,
      topProducts: ['Ivory Collarless Blazer', 'Sculpted Day Bag', 'Pearl Drop Earring Set'],
      trafficSources: ['Direct', 'Search', 'Social']
    }
  },
  {
    id: 'store-sports',
    merchantName: 'Northline Active',
    name: 'Northline Active',
    slug: 'demo-sports',
    category: 'Sports',
    description: 'Performance apparel, training extras, and compact sports equipment for active weekends.',
    template: 'sports',
    brandColor: '#245c7a',
    logoText: 'NA',
    currency: 'USD',
    shippingMessage: 'Free shipping over $90',
    announcement: 'Training kits, equipment, and active layers ready for the weekend.',
    heroTitle: 'Built for motion',
    heroText: 'Shop activewear, sports equipment, and everyday training essentials.',
    published: true,
    setup: {
      details: true,
      template: true,
      products: true,
      shipping: true,
      preview: true,
      publish: true
    },
    categories: ['New Arrivals', 'Activewear', 'Equipment', 'Bags', 'Shoes', 'Sale'],
    products: [
      product(2001, 'Pace Training Jacket', 'pace-training-jacket', 'Activewear', 84, null, 21, '/catalog/sportswear.svg', ['New'], 'A breathable training layer with zip pockets and a lightweight shell.'),
      product(2002, 'Compact Gym Duffel', 'compact-gym-duffel', 'Bags', 58, 72, 9, '/catalog/bags.svg', ['Sale'], 'A compact duffel with separate shoe storage and reinforced handles.'),
      product(2003, 'Grip Court Sneaker', 'grip-court-sneaker', 'Shoes', 118, null, 13, '/catalog/shoes.svg', ['Best Seller'], 'Court-ready sneakers with stable grip and everyday comfort.'),
      product(2004, 'Balance Mat Set', 'balance-mat-set', 'Equipment', 46, null, 6, '/catalog/accessories.svg', ['Low Stock'], 'A portable training mat with a carry strap and wipe-clean surface.'),
      product(2005, 'Core Run Short', 'core-run-short', 'Activewear', 42, 54, 17, '/catalog/sportswear.svg', ['Sale'], 'Quick-dry running shorts with a secure inner pocket.'),
      product(2006, 'Resistance Band Kit', 'resistance-band-kit', 'Equipment', 32, null, 28, '/catalog/new-arrivals.svg', ['New'], 'A five-piece resistance kit for home, travel, and warmups.')
    ],
    analytics: {
      sales: 11980,
      orders: 148,
      visitors: 4380,
      conversionRate: '3.4%',
      averageOrderValue: 80.94,
      topProducts: ['Grip Court Sneaker', 'Pace Training Jacket', 'Compact Gym Duffel'],
      trafficSources: ['Search', 'Partner clubs', 'Email']
    }
  },
  {
    id: 'store-home',
    merchantName: 'Harbor Home',
    name: 'Harbor Home',
    slug: 'demo-home',
    category: 'Home goods',
    description: 'Warm home pieces, thoughtful gifts, and minimal lifestyle goods for calm daily routines.',
    template: 'home',
    brandColor: '#6f614f',
    logoText: 'HH',
    currency: 'USD',
    shippingMessage: 'Free shipping on orders over $60',
    announcement: 'New home living pieces for quieter everyday rituals.',
    heroTitle: 'Make daily spaces softer',
    heroText: 'Shop home goods, gifts, and small lifestyle details from Harbor Home.',
    published: true,
    setup: {
      details: true,
      template: true,
      products: true,
      shipping: true,
      preview: true,
      publish: true
    },
    categories: ['New Arrivals', 'Home Living', 'Kitchen', 'Gifts', 'Textiles', 'Sale'],
    products: [
      product(3001, 'Linen Table Runner', 'linen-table-runner', 'Textiles', 48, null, 16, '/catalog/seasonal.svg', ['New'], 'A washed linen runner for simple dining tables and layered styling.'),
      product(3002, 'Stoneware Pourer', 'stoneware-pourer', 'Kitchen', 34, null, 10, '/catalog/accessories.svg', ['Best Seller'], 'A small stoneware pourer for sauces, cream, or countertop styling.'),
      product(3003, 'Soft Cotton Throw', 'soft-cotton-throw', 'Home Living', 72, 92, 7, '/catalog/new-arrivals.svg', ['Sale'], 'A breathable cotton throw with a subtle ribbed texture.'),
      product(3004, 'Cedar Drawer Sachet Set', 'cedar-drawer-sachet-set', 'Gifts', 22, null, 32, '/catalog/jewelry.svg', ['Gift'], 'A small cedar sachet set for drawers, wardrobes, and packaged gifts.'),
      product(3005, 'Minimal Brass Hook', 'minimal-brass-hook', 'Home Living', 18, null, 5, '/catalog/accessories.svg', ['Low Stock'], 'A compact brass wall hook for entryways, closets, and small spaces.'),
      product(3006, 'Ceramic Tea Cup Pair', 'ceramic-tea-cup-pair', 'Kitchen', 38, 46, 12, '/catalog/sale.svg', ['Sale'], 'A pair of handleless ceramic tea cups with a warm matte glaze.')
    ],
    analytics: {
      sales: 7420,
      orders: 96,
      visitors: 2980,
      conversionRate: '3.2%',
      averageOrderValue: 77.29,
      topProducts: ['Soft Cotton Throw', 'Stoneware Pourer', 'Linen Table Runner'],
      trafficSources: ['Direct', 'Email', 'Search']
    }
  }
]

function product(id, name, slug, category, price, compareAtPrice, stockQuantity, imageUrl, badges, description) {
  const discountPercent = compareAtPrice
    ? Math.round(((Number(compareAtPrice) - Number(price)) / Number(compareAtPrice)) * 100)
    : 0
  return {
    id,
    name,
    slug,
    category,
    price,
    compareAtPrice,
    effectivePrice: price,
    discountPercent,
    stockQuantity,
    lowStockThreshold: 6,
    imageUrl,
    imageGallery: [imageUrl],
    badges,
    status: stockQuantity > 0 ? 'ACTIVE' : 'ARCHIVED',
    description
  }
}

export function getTemplateById(templateId) {
  return storeTemplates.find((template) => template.id === templateId) || storeTemplates[0]
}

export function createSlug(value) {
  return String(value || '')
    .toLowerCase()
    .trim()
    .replace(/[^a-z0-9]+/g, '-')
    .replace(/^-+|-+$/g, '')
    || 'new-store'
}
