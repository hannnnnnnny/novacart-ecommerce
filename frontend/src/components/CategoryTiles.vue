<template>
  <div class="category-tile-grid">
    <RouterLink v-for="category in categories" :key="category" class="category-tile compact-category-tile" :to="{ path: productsPath, query: { category } }">
      <span>{{ category }}</span>
      <small>Shop {{ category.toLowerCase() }}</small>
    </RouterLink>
  </div>
</template>

<script setup>
defineProps({
  categories: {
    type: Array,
    required: true
  },
  productsPath: {
    type: String,
    required: true
  }
})
</script>
