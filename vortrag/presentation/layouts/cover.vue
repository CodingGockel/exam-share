<script setup lang="ts">
// Titelfolie nach Layout "Titelfolie mit Projekttitel" der Vorlage.
// Text kommt aus dem Frontmatter: subtitle (oben), Titel = Slot (h1), date (unten).
defineProps<{ subtitle?: string; date?: string }>()
</script>

<template>
  <div class="slidev-layout uni-cover">
    <img class="logo" src="/img/logo.png" alt="Friedrich-Schiller-Universität Jena" />
    <div class="gray-area" />
    <div class="bar-left" />
    <img class="bar-right" src="/img/farbstreifen.png" alt="" />

    <div class="subtitle">{{ subtitle }}</div>
    <div class="title"><slot /></div>
    <div class="date">{{ date }}</div>
  </div>
</template>

<style scoped>
.uni-cover { overflow: hidden; }
.logo { position: absolute; left: 4.3%; top: 8.7%; width: 17.2%; }
.gray-area { position: absolute; left: 0; right: 0; top: 58.4%; bottom: 0; background: var(--uni-gray); }
.bar-left { position: absolute; left: 0; width: 50.2%; top: 57.7%; height: 1.5%; background: var(--uni-navy-80); }
.bar-right { position: absolute; left: 50%; width: 50%; top: 57.7%; height: 1.5%; object-fit: fill; }
.subtitle, .title, .date {
  position: absolute;
  left: 5.1%;
  right: 5.1%;
  text-align: right;
}
.subtitle { top: 64.5%; font-size: 22px; color: var(--uni-navy); }
.title { top: 71%; }
.title :deep(h1) {
  font-family: var(--font-serif);
  font-size: 42px;
  font-weight: 400;
  margin: 0;
  line-height: 1.15;
}
.title :deep(p) { margin: 2px 0 0; font-size: 19px; color: var(--uni-blue); }
.date { bottom: 5%; font-size: 20px; }
</style>
