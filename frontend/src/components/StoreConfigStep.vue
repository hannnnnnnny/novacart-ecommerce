<template>
  <section class="onboarding-step">
    <p class="eyebrow">Step 4</p>
    <h2>Configure store</h2>
    <p>Set the public-facing brand details used by the generated storefront.</p>
    <div class="form-grid">
      <label>Logo text<input v-model.trim="model.logoText" maxlength="3" placeholder="AS" /></label>
      <label>Brand color<input v-model="model.brandColor" type="color" /></label>
      <label>Currency
        <select v-model="model.currency">
          <option>USD</option>
          <option>NZD</option>
          <option>AUD</option>
          <option>EUR</option>
          <option>GBP</option>
        </select>
      </label>
      <label class="wide-field">Shipping message<input v-model.trim="model.shippingMessage" placeholder="Free shipping on orders over $75" /></label>
    </div>
  </section>
</template>

<script setup>
defineProps({
  model: {
    type: Object,
    required: true
  }
})
</script>
