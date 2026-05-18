<template>
  <header class="platform-navbar">
    <RouterLink class="platform-brand" to="/" aria-label="NovaCart platform home">
      <span class="brand-mark" aria-hidden="true">N</span>
      <span>NovaCart</span>
    </RouterLink>
    <nav class="platform-nav" aria-label="Platform navigation">
      <RouterLink to="/features">Features</RouterLink>
      <RouterLink to="/templates">Templates</RouterLink>
      <RouterLink to="/pricing">Pricing</RouterLink>
      <RouterLink to="/store/demo-fashion">Demo store</RouterLink>
    </nav>
    <div class="platform-nav-actions">
      <RouterLink class="text-link" to="/login">Log in</RouterLink>
      <RouterLink class="primary-button" to="/onboarding">Start building</RouterLink>
    </div>
  </header>
</template>
