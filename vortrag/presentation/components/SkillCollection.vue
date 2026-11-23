<script setup lang="ts">
// Die wachsende Skill-Sammlung (roter Faden, siehe gliederung.md 2.2).
// current: Name des Skills, der in diesem Kapitel dazukommt.
import { computed } from 'vue'

const props = defineProps<{ current: string }>()

const skills = [
  { name: 'unit-testing', part: '3.1' },
  { name: 'api-testing', part: '3.2' },
  { name: 'e2e-playwright', part: '3.3' },
  { name: 'ui-visual-check', part: '3.4' },
  { name: 'regression-bugfix', part: '3.5' },
  { name: 'security-testing', part: '4.1' },
  { name: 'load-testing', part: '4.2' },
  { name: 'test-review', part: '5' },
]
const idx = computed(() => skills.findIndex(s => s.name === props.current))
</script>

<template>
  <div class="skills">
    <div class="head"><span class="i-carbon-folder" /> .claude/skills/</div>
    <div
      v-for="(s, i) in skills"
      :key="s.name"
      class="row"
      :class="{ done: i < idx, current: i === idx, todo: i > idx }"
    >
      <span v-if="i < idx" class="i-carbon-checkmark-filled st" />
      <span v-else-if="i === idx" class="i-carbon-add-filled st" />
      <span v-else class="i-carbon-circle-dash st" />
      <span class="name">{{ s.name }}/</span>
      <span class="part">{{ s.part }}</span>
    </div>
  </div>
</template>

<style scoped>
.skills { font-family: var(--font-mono); font-size: 13px; background: #f5f6f9; border-radius: 8px; padding: 10px 12px; }
.head { font-weight: 700; display: flex; align-items: center; gap: 6px; margin-bottom: 6px; }
.row { display: flex; align-items: center; gap: 6px; padding: 2px 4px 2px 14px; border-radius: 4px; }
.row .name { flex: 1; }
.part { font-size: 11px; font-family: var(--font-sans); color: #8a96ab; }
.done { color: var(--uni-blue); }
.done .st { color: var(--ok); }
.current { background: var(--uni-navy); color: #fff; font-weight: 700; }
.current .st, .current .part { color: #f0dfaa; }
.todo { color: #b3bccb; }
</style>
