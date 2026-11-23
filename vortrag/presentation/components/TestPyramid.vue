<script setup lang="ts">
// Testpyramide mit hervorhebbaren Ebenen.
// active: eine oder mehrere von 'unit' | 'api' | 'component' | 'e2e' | 'regression'
// axes: zeigt links/rechts die Achsen (Anzahl vs. Kosten/Laufzeit)
// labels: Untertitel je Ebene (Tools)
import { computed } from 'vue'

const props = withDefaults(defineProps<{
  active?: string | string[]
  axes?: boolean
  tools?: boolean
  width?: number
}>(), { axes: false, tools: false, width: 420 })

const act = computed(() => new Set(Array.isArray(props.active) ? props.active : props.active ? [props.active] : []))
const anyActive = computed(() => [...act.value].some(id => id !== 'regression'))
const fill = (id: string, base: string) => (!anyActive.value || act.value.has(id) ? base : '#e4e7ee')
const txt = (id: string) => (!anyActive.value || act.value.has(id) ? '#fff' : '#8a96ab')
</script>

<template>
  <svg :width="props.width" :viewBox="`0 0 420 300`" class="pyramid" role="img" aria-label="Testpyramide">
    <!-- E2E -->
    <polygon points="210,10 264,92 156,92" :fill="fill('e2e', '#002f5d')" />
    <text x="210" y="70" text-anchor="middle" :fill="txt('e2e')" style="font-size: 17px" font-weight="700">E2E</text>
    <!-- Integration: API | Komponenten -->
    <polygon points="152,98 210,98 210,180 98,180" :fill="fill('api', '#7682a5')" />
    <polygon points="210,98 268,98 322,180 210,180" :fill="fill('component', '#8e98b7')" />
    <text x="172" y="135" text-anchor="middle" :fill="txt('api')" style="font-size: 16px" font-weight="700">API</text>
    <text x="172" y="153" text-anchor="middle" :fill="txt('api')" style="font-size: 11px">Backend + DB</text>
    <text x="250" y="135" text-anchor="middle" :fill="txt('component')" style="font-size: 16px" font-weight="700">UI-Komp.</text>
    <text x="250" y="153" text-anchor="middle" :fill="txt('component')" style="font-size: 11px">Frontend</text>
    <!-- Unit -->
    <polygon points="94,186 326,186 382,270 38,270" :fill="fill('unit', '#ae9a63')" />
    <text x="210" y="234" text-anchor="middle" :fill="txt('unit')" style="font-size: 21px" font-weight="700">Unit</text>

    <!-- Regression als Querschnitt über alle Ebenen -->
    <g v-if="act.has('regression')">
      <rect x="186" y="4" width="48" height="272" rx="6" fill="#c8282d" opacity="0.18" />
      <rect x="186" y="4" width="48" height="272" rx="6" fill="none" stroke="#c8282d" stroke-width="2" stroke-dasharray="5 4" />
      <text x="210" y="292" text-anchor="middle" fill="#c8282d" style="font-size: 12px" font-weight="700">Regression = Zweck, keine Ebene</text>
    </g>

    <g v-if="props.tools" style="font-size: 13px" fill="#5a6b80">
      <text x="276" y="40">Playwright</text>
      <text x="276" y="54">Agent + Browser</text>
      <text x="330" y="132">Vitest +</text>
      <text x="330" y="146">Vue Test Utils</text>
      <text x="8" y="132">Testcontainers</text>
      <text x="8" y="146">Spring Boot Test</text>
      <text x="300" y="290">JUnit 5, AssertJ, jqwik</text>
    </g>

    <g v-if="props.axes" style="font-size: 11px" fill="#5a6b80">
      <line x1="20" y1="260" x2="70" y2="40" stroke="#ae9a63" stroke-width="2" marker-end="url(#arr)" />
      <text x="28" y="30">Kosten, Laufzeit, Realismus ↑</text>
      <line x1="400" y1="40" x2="350" y2="260" stroke="#ae9a63" stroke-width="2" marker-end="url(#arr)" />
      <text x="330" y="290" text-anchor="middle">Anzahl der Tests ↓</text>
    </g>
    <defs>
      <marker id="arr" viewBox="0 0 10 10" refX="8" refY="5" markerWidth="6" markerHeight="6" orient="auto-start-reverse">
        <path d="M 0 0 L 10 5 L 0 10 z" fill="#ae9a63" />
      </marker>
    </defs>
  </svg>
</template>

<style scoped>
.pyramid { font-family: var(--font-sans); overflow: visible; }
</style>
