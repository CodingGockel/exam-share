<script setup lang="ts">
// Kreislauf (z. B. Agent-Loop oder Red-Green-Refactor). 3–5 Schritte im Uhrzeigersinn.
import { computed } from 'vue'

const props = withDefaults(defineProps<{
  steps: { title: string; icon?: string; color?: string }[]
  center?: string
  size?: number
}>(), { size: 300 })

const R = 38 // Radius in % der Größe
const nodes = computed(() =>
  props.steps.map((s, i) => {
    const a = -Math.PI / 2 + (i * 2 * Math.PI) / props.steps.length
    return { ...s, x: 50 + R * Math.cos(a), y: 50 + R * Math.sin(a) }
  }),
)
const arcs = computed(() => {
  const n = props.steps.length
  const r = R
  return props.steps.map((_, i) => {
    const gap = 0.42
    const a1 = -Math.PI / 2 + (i * 2 * Math.PI) / n + gap
    const a2 = -Math.PI / 2 + ((i + 1) * 2 * Math.PI) / n - gap
    return `M ${50 + r * Math.cos(a1)} ${50 + r * Math.sin(a1)} A ${r} ${r} 0 0 1 ${50 + r * Math.cos(a2)} ${50 + r * Math.sin(a2)}`
  })
})
</script>

<template>
  <div class="cycle" :style="{ width: `${props.size}px`, height: `${props.size}px` }">
    <svg viewBox="0 0 100 100" class="arcs">
      <defs>
        <marker id="cyc-arrow" viewBox="0 0 10 10" refX="7" refY="5" markerWidth="5" markerHeight="5" orient="auto">
          <path d="M0 0 L10 5 L0 10 z" fill="#ae9a63" />
        </marker>
      </defs>
      <path v-for="(d, i) in arcs" :key="i" :d="d" fill="none" stroke="#ae9a63" stroke-width="1.6" marker-end="url(#cyc-arrow)" />
    </svg>
    <div v-if="props.center" class="center">{{ props.center }}</div>
    <div
      v-for="n in nodes"
      :key="n.title"
      class="node"
      :class="n.color ?? 'navy'"
      :style="{ left: `${n.x}%`, top: `${n.y}%` }"
    >
      <span v-if="n.icon" :class="n.icon" class="ico" />
      <span class="t">{{ n.title }}</span>
    </div>
  </div>
</template>

<style scoped>
.cycle { position: relative; flex: none; }
.arcs { position: absolute; inset: 0; width: 100%; height: 100%; }
.center {
  position: absolute;
  inset: 34%;
  display: flex;
  align-items: center;
  justify-content: center;
  text-align: center;
  font-family: var(--font-serif);
  font-size: 15px;
  color: var(--uni-navy);
  line-height: 1.2;
}
.node {
  position: absolute;
  transform: translate(-50%, -50%);
  border-radius: 20px;
  padding: 5px 11px;
  font-size: 13px;
  font-weight: 700;
  color: #fff;
  display: flex;
  align-items: center;
  gap: 5px;
  white-space: nowrap;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.12);
}
.ico { font-size: 16px; }
.navy { background: var(--uni-navy); }
.gold { background: var(--uni-gold); }
.blue { background: var(--uni-blue); }
.ok { background: var(--ok); }
.bad { background: var(--bad); }
</style>
