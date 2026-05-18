<template>
  <section class="onboarding-step">
    <p class="eyebrow">Step 1</p>
    <h2>Store basics</h2>
    <p>Give the merchant store a name, category, and public URL slug.</p>
    <div class="form-grid">
      <label>Store name<input v-model.trim="model.name" required placeholder="Avery Studio" /></label>
      <label>Store slug<input v-model.trim="model.slug" required placeholder="avery-studio" /></label>
      <label>Store category
        <select v-model="model.category">
          <option>Fashion</option>
          <option>Lifestyle</option>
          <option>Sports</option>
          <option>Home goods</option>
          <option>Thrift and vintage</option>
        </select>
      </label>
      <label class="wide-field">Store description<textarea v-model.trim="model.description" rows="4" placeholder="Independent products, thoughtful edits, and a clear customer promise."></textarea></label>
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
