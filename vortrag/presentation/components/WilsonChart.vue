<script setup lang="ts">
// Naiver Anteil vs. untere Grenze des Wilson-Intervalls (z = 1,96), live berechnet.
// Zeigt, warum "1 von 1" nicht vor "95 von 100" landen darf.
import { computed } from 'vue'

const props = withDefaults(defineProps<{ examples?: [number, number][] }>(), {
  examples: () => [[1, 0], [95, 5], [9, 1], [4, 1], [2, 3], [0, 0]],
})

const Z = 1.96
function wilson(up: number, down: number): number {
  const n = up + down
  if (n === 0) return 0
  const p = up / n
  const z2 = Z * Z
  return (p + z2 / (2 * n) - Z * Math.sqrt((p * (1 - p) + z2 / (4 * n)) / n)) / (1 + z2 / n)
}

const rows = computed(() => {
  const r = props.examples.map(([up, down]) => ({
    up, down,
    naive: up + down === 0 ? 0 : up / (up + down),
    wilson: wilson(up, down),
  }))
  const naiveRank = [...r].sort((a, b) => b.naive - a.naive)
  const wilsonRank = [...r].sort((a, b) => b.wilson - a.wilson)
  return wilsonRank.map(x => ({ ...x, naiveRank: naiveRank.indexOf(x) + 1, wilsonRank: wilsonRank.indexOf(x) + 1 }))
})
const fmt = (v: number) => v.toFixed(2).replace('.', ',')
</script>

<template>
  <div class="wilson">
    <div class="legend">
      <span><i class="sw naive" /> naiver Anteil 👍 / gesamt</span>
      <span><i class="sw wil" /> Wilson-Untergrenze (Score)</span>
    </div>
    <div v-for="r in rows" :key="`${r.up}-${r.down}`" class="row">
      <div class="lbl">
        <span class="rank">#{{ r.wilsonRank }}</span>
        {{ r.up }} 👍 · {{ r.down }} 👎
        <span v-if="r.naiveRank !== r.wilsonRank" class="was">naiv #{{ r.naiveRank }}</span>
      </div>
      <div class="bars">
        <div class="bar naive" :style="{ width: `${r.naive * 100}%` }"><span>{{ fmt(r.naive) }}</span></div>
        <div class="bar wil" :style="{ width: `${Math.max(r.wilson * 100, 0.5)}%` }"><span>{{ fmt(r.wilson) }}</span></div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.wilson { font-size: 13px; width: 100%; }
.legend { display: flex; gap: 16px; margin-bottom: 6px; color: var(--uni-text-muted); }
.sw { display: inline-block; width: 12px; height: 10px; border-radius: 2px; margin-right: 4px; vertical-align: middle; }
.row { display: grid; grid-template-columns: 175px 1fr; align-items: center; gap: 8px; margin: 3px 0; }
.lbl { font-weight: 600; white-space: nowrap; }
.rank { display: inline-block; width: 24px; color: var(--uni-gold); }
.was { font-weight: 400; font-size: 11px; color: var(--bad); margin-left: 4px; }
.bars { display: flex; flex-direction: column; gap: 2px; }
.bar { height: 11px; border-radius: 0 3px 3px 0; position: relative; min-width: 2px; }
.bar span { position: absolute; left: calc(100% + 4px); top: -3px; font-size: 11px; color: var(--uni-text-muted); white-space: nowrap; }
.naive, .sw.naive { background: var(--uni-blue-pale); }
.wil, .sw.wil { background: var(--uni-navy); }
</style>
