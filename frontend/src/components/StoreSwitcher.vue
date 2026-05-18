<template>
  <label class="store-switcher">
    <span>Current store</span>
    <select v-model="selectedSlug" @change="changeStore">
      <option v-for="store in platformStore.stores" :key="store.slug" :value="store.slug">
        {{ store.name }}
      </option>
    </select>
  </label>
</template>

<script setup>
import { onMounted, ref, watch } from 'vue'
import { usePlatformStore } from '../stores/platform'

const platformStore = usePlatformStore()
const selectedSlug = ref(platformStore.currentStoreSlug)

onMounted(() => {
  platformStore.loadPlatform()
  selectedSlug.value = platformStore.currentStoreSlug
})

watch(
  () => platformStore.currentStoreSlug,
  (value) => {
    selectedSlug.value = value
  }
)

function changeStore() {
  platformStore.setCurrentStore(selectedSlug.value)
}
</script>
