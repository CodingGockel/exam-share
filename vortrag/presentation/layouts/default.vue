<script setup lang="ts">
// Inhaltsfolie nach Layout "Überschrift und Text | grau" der Vorlage:
// kurzer Navy-Strich über dem Titel, grauer Fußbereich mit Logo, Farbstreifen,
// Veranstaltung, Vortragende | Datum und Seitenzahl.
//
// Frontmatter-Optionen:
//   step: 1–5   blendet oben rechts die Themen-Leiste (TopicSteps) ein
//   topic: Text links neben der Leiste (z. B. "3.1 Unit-Tests")
import { useSlideContext } from '@slidev/client'
import { computed } from 'vue'

const { $page, $frontmatter, $slidev } = useSlideContext()
const configs = computed(() => $slidev.configs as Record<string, any>)
</script>

<template>
  <div class="slidev-layout uni-default">
    <div class="title-line" />
    <TopicSteps v-if="$frontmatter.step" class="steps" :step="$frontmatter.step" :topic="$frontmatter.topic" />

    <div class="content">
      <slot />
    </div>

    <div class="footer">
      <div class="bar-left" />
      <img class="bar-right" src="/img/farbstreifen.png" alt="" />
      <img class="logo" src="/img/logo.png" alt="Friedrich-Schiller-Universität Jena" />
      <div class="event">{{ configs.event }}</div>
      <div class="author">{{ configs.author }} | {{ configs.date }}</div>
      <div class="page">{{ $page }}</div>
    </div>
  </div>
</template>

<style scoped>
.uni-default { overflow: hidden; }
.title-line {
  position: absolute;
  left: 4.9%;
  top: 6.6%;
  width: 4.9%;
  height: 4.5px;
  background: var(--uni-navy);
}
.steps { position: absolute; right: 4.9%; top: 3.2%; }

.content {
  position: absolute;
  left: 4.9%;
  right: 4.9%;
  top: 8.7%;
  bottom: 14.8%;
  display: flex;
  flex-direction: column;
}
.content :deep(h1 code) { background: transparent; padding: 0; font-size: 0.92em; }
.content :deep(h1) {
  font-family: var(--font-serif);
  font-size: 32px;
  font-weight: 500;
  margin: 0 0 18px 0;
  color: var(--uni-navy);
  line-height: 1.2;
}

.footer { position: absolute; left: 0; right: 0; bottom: 0; top: 87.5%; background: var(--uni-gray); }
.bar-left { position: absolute; left: 0; width: 50.2%; top: 0; height: 4px; background: var(--uni-navy-80); }
.bar-right { position: absolute; left: 50%; width: 50%; top: 0; height: 4.5px; object-fit: fill; }
.logo { position: absolute; left: 4.4%; top: 22%; width: 11.4%; }
.event, .author {
  position: absolute;
  right: 9.9%;
  font-size: 13px;
  text-align: right;
  white-space: nowrap;
}
.event { top: 32%; }
.author { top: 59%; }
.page { position: absolute; right: 4.9%; top: 59%; font-size: 13px; font-weight: 600; }
</style>
