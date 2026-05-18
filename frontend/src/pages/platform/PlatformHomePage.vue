<template>
  <div>
    <PlatformHero />
    <FeatureGrid :features="platformFeatures" />
    <TemplateShowcase :templates="storeTemplates" />
    <section class="platform-section analytics-preview-section">
      <div class="platform-section-heading">
        <p class="eyebrow">Analytics preview</p>
        <h2>Know what is happening inside each store</h2>
      </div>
      <div class="preview-kpi-row analytics-preview-row">
        <article><span>Total sales</span><strong>$18.4k</strong></article>
        <article><span>Orders</span><strong>214</strong></article>
        <article><span>Conversion rate</span><strong>3.8%</strong></article>
        <article><span>Top product</span><strong>Ivory Blazer</strong></article>
      </div>
    </section>
    <WorkflowSteps :steps="workflowSteps" />
    <PricingCards :plans="pricingPlans" />
    <PlatformCTA />
  </div>
</template>

<script setup>
import FeatureGrid from '../../components/FeatureGrid.vue'
import PlatformCTA from '../../components/PlatformCTA.vue'
import PlatformHero from '../../components/PlatformHero.vue'
import PricingCards from '../../components/PricingCards.vue'
import TemplateShowcase from '../../components/TemplateShowcase.vue'
import WorkflowSteps from '../../components/WorkflowSteps.vue'
import { platformFeatures, pricingPlans, storeTemplates, workflowSteps } from '../../data/platform'
</script>
