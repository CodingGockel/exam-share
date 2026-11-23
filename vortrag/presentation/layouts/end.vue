<script setup lang="ts">
// Schlussfolie nach Layout "Schlussfolie" der Vorlage.
defineProps<{ name?: string }>()
</script>

<template>
  <div class="slidev-layout uni-end">
    <img class="logo" src="/img/logo.png" alt="Friedrich-Schiller-Universität Jena" />
    <div class="gray-area" />
    <div class="bar-left" />
    <img class="bar-right" src="/img/farbstreifen.png" alt="" />

    <div class="title"><slot /></div>
    <div class="name">{{ name ?? $slidev.configs.author }}</div>
  </div>
</template>

<style scoped>
.uni-end { overflow: hidden; }
.logo { position: absolute; left: 4.3%; top: 8.7%; width: 17.2%; }
.gray-area { position: absolute; left: 0; right: 0; top: 69.2%; bottom: 0; background: var(--uni-gray); }
.bar-left { position: absolute; left: 0; width: 50.2%; top: 68.5%; height: 1.5%; background: var(--uni-navy-80); }
.bar-right { position: absolute; left: 50%; width: 50%; top: 68.5%; height: 1.5%; object-fit: fill; }
.title, .name { position: absolute; left: 5.1%; right: 5.1%; text-align: right; }
.title { top: 76.7%; }
.title :deep(h1) { font-family: var(--font-serif); font-size: 42px; font-weight: 400; margin: 0; }
.title :deep(p) { margin: 0; }
.name { bottom: 7.5%; font-size: 26px; }
</style>
