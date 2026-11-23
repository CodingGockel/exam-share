<script setup lang="ts">
// Äquivalenzklassen und Grenzwerte auf einer Achse.
// zones: Bereiche von links nach rechts (flex = relative Breite, ok = gültig?)
// ticks: Testwerte an Position at (0–1), ok = erwartetes Ergebnis
const props = defineProps<{
  title?: string
  zones: { label: string; flex?: number; ok: boolean }[]
  ticks: { at: number; label: string; ok: boolean }[]
}>()
</script>

<template>
  <div class="boundary">
    <div v-if="props.title" class="title">{{ props.title }}</div>
    <div class="track">
      <div
        v-for="z in props.zones"
        :key="z.label"
        class="zone"
        :class="z.ok ? 'zok' : 'zbad'"
        :style="{ flex: z.flex ?? 1 }"
      >
        {{ z.label }}
      </div>
      <div v-for="t in props.ticks" :key="t.label" class="tick" :style="{ left: `${t.at * 100}%` }">
        <div class="pin" :class="t.ok ? 'pok' : 'pbad'" />
        <div class="tlabel" :class="t.ok ? 'ok' : 'bad'">
          <span :class="t.ok ? 'i-carbon-checkmark-filled' : 'i-carbon-close-filled'" />
          {{ t.label }}
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.boundary { width: 100%; margin-bottom: 26px; }
.title { font-weight: 700; font-size: 15px; margin-bottom: 4px; }
.track { position: relative; display: flex; height: 30px; border-radius: 6px; overflow: visible; }
.zone {
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 12px;
  font-weight: 600;
}
.zone:first-child { border-radius: 6px 0 0 6px; }
.zone:nth-last-child(1 of .zone) { border-radius: 0 6px 6px 0; }
.zok { background: var(--ok-bg); color: var(--ok); }
.zbad { background: var(--bad-bg); color: var(--bad); }
.tick { position: absolute; top: -4px; transform: translateX(-50%); display: flex; flex-direction: column; align-items: center; }
.pin { width: 3px; height: 38px; border-radius: 2px; flex: none; }
.pok { background: var(--ok); }
.pbad { background: var(--bad); }
.tlabel { font-size: 11.5px; font-weight: 700; white-space: nowrap; display: flex; gap: 2px; align-items: center; margin-top: 1px; }
</style>
