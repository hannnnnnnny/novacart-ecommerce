<template>
  <div>
    <section class="platform-page-hero">
      <p class="eyebrow">Pricing</p>
      <h1>Demo plans for a merchant website builder</h1>
      <p>Pricing is visual only in this portfolio project. No billing provider is connected.</p>
    </section>
    <PricingCards :plans="pricingPlans" />
    <PlatformCTA />
  </div>
</template>

<script setup>
import PlatformCTA from '../../components/PlatformCTA.vue'
import PricingCards from '../../components/PricingCards.vue'
import { pricingPlans } from '../../data/platform'
</script>
