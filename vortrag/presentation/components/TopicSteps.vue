<script setup lang="ts">
// Leiste mit dem festen Schema jedes Themas (siehe gliederung.md, "Aufbau jedes Themas").
// Wird vom default-Layout eingeblendet, sobald im Frontmatter `step` gesetzt ist.
const props = defineProps<{ step: number; topic?: string }>()

const steps = [
  { label: 'Was & Wozu', icon: 'i-carbon-help' },
  { label: 'Klassisch', icon: 'i-carbon-tool-box' },
  { label: 'KI-Demo', icon: 'i-carbon-play-filled' },
  { label: 'Bewertung', icon: 'i-carbon-scales' },
  { label: 'Skill', icon: 'i-carbon-skill-level' },
]
</script>

<template>
  <div class="topic-steps">
    <span v-if="props.topic" class="topic">{{ props.topic }}</span>
    <span
      v-for="(s, i) in steps"
      :key="s.label"
      class="chip"
      :class="{ active: i + 1 === props.step, done: i + 1 < props.step }"
    >
      <span :class="s.icon" class="ico" />
      <span class="lbl">{{ s.label }}</span>
    </span>
  </div>
</template>

<style scoped>
.topic-steps { display: flex; align-items: center; gap: 4px; font-size: 11px; }
.topic { font-weight: 700; color: var(--uni-gold); margin-right: 6px; letter-spacing: 0.03em; }
.chip {
  display: inline-flex;
  align-items: center;
  gap: 3px;
  padding: 2px 7px;
  border-radius: 10px;
  background: #eef0f5;
  color: #8a96ab;
}
.chip.done { color: var(--uni-blue); }
.chip.active { background: var(--uni-navy); color: #fff; font-weight: 600; }
.ico { font-size: 11px; }
</style>
