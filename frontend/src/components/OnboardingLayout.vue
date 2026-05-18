<template>
  <main class="onboarding-shell">
    <RouterLink class="platform-brand onboarding-brand" to="/" aria-label="NovaCart platform home">
      <span class="brand-mark" aria-hidden="true">N</span>
      <span>NovaCart</span>
    </RouterLink>
    <section class="onboarding-panel">
      <aside class="onboarding-steps" aria-label="Onboarding progress">
        <p class="eyebrow">Store setup</p>
        <h1>Create a merchant store</h1>
        <ol>
          <li v-for="(item, index) in steps" :key="item" :class="{ active: index === currentStep, complete: index < currentStep }">
            <span>{{ index + 1 }}</span>
            {{ item }}
          </li>
        </ol>
      </aside>
      <div class="onboarding-content">
        <slot />
      </div>
    </section>
  </main>
</template>

<script setup>
defineProps({
  steps: {
    type: Array,
    required: true
  },
  currentStep: {
    type: Number,
    required: true
  }
})
</script>
