<template>
  <div>
    <section class="platform-page-hero">
      <p class="eyebrow">Platform features</p>
      <h1>Build, manage, and improve many merchant stores</h1>
      <p>NovaCart separates the platform website, merchant workspace, and generated storefronts while keeping commerce operations connected.</p>
      <RouterLink class="primary-button" to="/onboarding">Start building</RouterLink>
    </section>
    <FeatureGrid :features="platformFeatures" />
  </div>
</template>

<script setup>
import FeatureGrid from '../../components/FeatureGrid.vue'
import { platformFeatures } from '../../data/platform'
</script>
