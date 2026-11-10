<script setup lang="ts">
import { RouterLink, RouterView } from 'vue-router'
</script>

<template>
  <header class="site-header">
    <div class="site-header-inner">
      <RouterLink to="/" class="brand" data-testid="nav-home">Prüfprot</RouterLink>
      <nav aria-label="Hauptnavigation">
        <RouterLink :to="{ name: 'search' }" data-testid="nav-search">Suchen</RouterLink>
        <RouterLink :to="{ name: 'upload' }" data-testid="nav-upload">Hochladen</RouterLink>
      </nav>
    </div>
  </header>

  <main class="page">
    <RouterView />
  </main>

  <footer class="site-footer">Prüfprot – Altklausuren von Studierenden für Studierende</footer>
</template>
