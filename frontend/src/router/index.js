import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '../stores/auth'
import StorefrontLayout from '../layouts/StorefrontLayout.vue'
import AdminLayout from '../layouts/AdminLayout.vue'
import HomePage from '../pages/HomePage.vue'
import ProductListPage from '../pages/ProductListPage.vue'
import ProductDetailPage from '../pages/ProductDetailPage.vue'
import CartPage from '../pages/CartPage.vue'
import CheckoutPage from '../pages/CheckoutPage.vue'
import OrderSuccessPage from '../pages/OrderSuccessPage.vue'
import SupportPage from '../pages/SupportPage.vue'
import RefundRequestPage from '../pages/RefundRequestPage.vue'
import AdminLoginPage from '../pages/admin/AdminLoginPage.vue'
import AdminDashboardPage from '../pages/admin/AdminDashboardPage.vue'
import AdminProductsPage from '../pages/admin/AdminProductsPage.vue'
import AdminProductFormPage from '../pages/admin/AdminProductFormPage.vue'
import AdminCategoriesPage from '../pages/admin/AdminCategoriesPage.vue'
import AdminCollectionsPage from '../pages/admin/AdminCollectionsPage.vue'
import AdminPromotionsPage from '../pages/admin/AdminPromotionsPage.vue'
import AdminOrdersPage from '../pages/admin/AdminOrdersPage.vue'
import AdminOrderDetailPage from '../pages/admin/AdminOrderDetailPage.vue'
import AdminInventoryPage from '../pages/admin/AdminInventoryPage.vue'
import AdminSupportPage from '../pages/admin/AdminSupportPage.vue'
import AdminRefundsPage from '../pages/admin/AdminRefundsPage.vue'
import AdminAnalyticsPage from '../pages/admin/AdminAnalyticsPage.vue'
import AdminCustomersPage from '../pages/admin/AdminCustomersPage.vue'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      component: StorefrontLayout,
      children: [
        { path: '', name: 'home', component: HomePage },
        { path: 'products', name: 'products', component: ProductListPage },
        { path: 'products/:id', name: 'product-detail', component: ProductDetailPage },
        { path: 'cart', name: 'cart', component: CartPage },
        { path: 'checkout', name: 'checkout', component: CheckoutPage },
        { path: 'order-success/:id', name: 'order-success', component: OrderSuccessPage },
        { path: 'support', name: 'support', component: SupportPage },
        { path: 'refund-request', name: 'refund-request', component: RefundRequestPage }
      ]
    },
    { path: '/admin/login', name: 'admin-login', component: AdminLoginPage },
    {
      path: '/admin',
      component: AdminLayout,
      meta: { requiresAuth: true },
      children: [
        { path: 'dashboard', name: 'admin-dashboard', component: AdminDashboardPage },
        { path: 'products', name: 'admin-products', component: AdminProductsPage },
        { path: 'products/new', name: 'admin-product-new', component: AdminProductFormPage },
        { path: 'products/:id/edit', name: 'admin-product-edit', component: AdminProductFormPage },
        { path: 'categories', name: 'admin-categories', component: AdminCategoriesPage },
        { path: 'collections', name: 'admin-collections', component: AdminCollectionsPage },
        { path: 'promotions', name: 'admin-promotions', component: AdminPromotionsPage },
        { path: 'orders', name: 'admin-orders', component: AdminOrdersPage },
        { path: 'orders/:id', name: 'admin-order-detail', component: AdminOrderDetailPage },
        { path: 'inventory', name: 'admin-inventory', component: AdminInventoryPage },
        { path: 'support', name: 'admin-support', component: AdminSupportPage },
        { path: 'refunds', name: 'admin-refunds', component: AdminRefundsPage },
        { path: 'customers', name: 'admin-customers', component: AdminCustomersPage },
        { path: 'analytics', name: 'admin-analytics', component: AdminAnalyticsPage }
      ]
    }
  ],
  scrollBehavior() {
    return { top: 0 }
  }
})

router.beforeEach((to) => {
  const authStore = useAuthStore()
  const sessionStatus = authStore.token ? authStore.validateSession() : authStore.loadSession()

  if (to.meta.requiresAuth && !authStore.isAuthenticated) {
    return {
      name: 'admin-login',
      query: {
        redirect: to.fullPath,
        ...(sessionStatus === 'expired' ? { session: 'expired' } : {})
      }
    }
  }
})

export default router
