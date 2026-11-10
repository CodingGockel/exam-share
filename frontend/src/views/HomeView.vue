<script setup lang="ts">
import { ref } from 'vue'
import { RouterLink } from 'vue-router'
import SearchForm from '../components/SearchForm.vue'

const searchOpen = ref(false)
</script>

<template>
  <section class="hero">
    <h1>Altklausuren finden und teilen</h1>
    <p class="hero-text">
      Prüfprot sammelt Altklausuren von Studierenden für Studierende. Ohne Anmeldung, einfach suchen, herunterladen
      und bewerten.
    </p>
    <div class="hero-actions">
      <button
        type="button"
        class="primary large"
        :aria-expanded="searchOpen"
        data-testid="home-search-toggle"
        @click="searchOpen = !searchOpen"
      >
        Klausur suchen
      </button>
      <RouterLink :to="{ name: 'upload' }" class="button large" data-testid="home-upload-link">
        Klausur hochladen
      </RouterLink>
    </div>
  </section>

  <SearchForm v-if="searchOpen" />

  <section class="steps" aria-label="So funktioniert's">
    <div class="step">
      <h2>Suchen</h2>
      <p>Uni und Modul eingeben, bei Bedarf nach Studiengang, Abschluss oder Semester filtern.</p>
    </div>
    <div class="step">
      <h2>Herunterladen</h2>
      <p>Alle Klausuren sind PDFs und lassen sich direkt herunterladen.</p>
    </div>
    <div class="step">
      <h2>Bewerten</h2>
      <p>Mit 👍 und 👎 landen hilfreiche Klausuren oben, unbrauchbare werden eingeklappt.</p>
    </div>
  </section>
</template>
