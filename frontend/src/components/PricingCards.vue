<template>
  <section class="platform-section pricing-section">
    <div class="platform-section-heading">
      <p class="eyebrow">Demo pricing</p>
      <h2>Plans for different merchant stages</h2>
      <p>These cards communicate SaaS packaging for the portfolio demo. No billing is connected.</p>
    </div>
    <div class="pricing-grid">
      <article v-for="plan in plans" :key="plan.name" class="pricing-card" :class="{ featured: plan.name === 'Growth' }">
        <span v-if="plan.name === 'Growth'" class="pricing-card-badge">Most balanced</span>
        <h3>{{ plan.name }}</h3>
        <strong>{{ plan.price }}</strong>
        <p>{{ plan.description }}</p>
        <ul>
          <li v-for="feature in plan.features" :key="feature">{{ feature }}</li>
        </ul>
        <RouterLink class="primary-button" to="/onboarding">Create store</RouterLink>
      </article>
    </div>
  </section>
</template>

<script setup>
defineProps({
  plans: {
    type: Array,
    required: true
  }
})
</script>
