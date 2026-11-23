<script setup lang="ts">
// Architektur von Prüfprot als Kette; hervorgehoben ist, was ein Test der jeweiligen
// Ebene wirklich ausführt. Ersetzte Teile (Mock/Stub) sind gestrichelt.
// scope: 'unit' | 'component' | 'api' | 'e2e'
import { computed } from 'vue'

const props = defineProps<{ scope: 'unit' | 'component' | 'api' | 'e2e' }>()

const parts = [
  { id: 'browser', label: 'Browser', sub: 'Vue-App', icon: 'i-logos-vue' },
  { id: 'nginx', label: 'nginx', sub: 'Proxy', icon: 'i-logos-nginx' },
  { id: 'controller', label: 'Controller', sub: 'REST /api', icon: 'i-carbon-api' },
  { id: 'service', label: 'Service', sub: 'Fachlogik', icon: 'i-carbon-function-math' },
  { id: 'repository', label: 'Repository', sub: 'JPA / SQL', icon: 'i-carbon-data-structured' },
  { id: 'db', label: 'PostgreSQL', sub: 'Docker', icon: 'i-logos-postgresql' },
]

const config = {
  unit: { real: ['service'], mocked: ['repository'], label: 'Unit-Test: eine Klasse, Abhängigkeiten ersetzt' },
  component: { real: ['browser'], mocked: ['nginx'], label: 'Komponententest: Vue-Komponente, API gemockt' },
  api: { real: ['controller', 'service', 'repository', 'db'], mocked: [], label: 'Integrationstest: echter HTTP-Aufruf bis in echtes Postgres' },
  e2e: { real: ['browser', 'nginx', 'controller', 'service', 'repository', 'db'], mocked: [], label: 'E2E: kompletter Stack, wie ein Nutzer' },
} as const

const cfg = computed(() => config[props.scope])
const state = (id: string) =>
  (cfg.value.real as readonly string[]).includes(id) ? 'real' : (cfg.value.mocked as readonly string[]).includes(id) ? 'mock' : 'off'
</script>

<template>
  <div class="scope-stack">
    <div class="row">
      <template v-for="(p, i) in parts" :key="p.id">
        <div class="box" :class="state(p.id)">
          <span :class="p.icon" class="ico" />
          <div class="lbl">{{ p.label }}</div>
          <div class="sub">{{ state(p.id) === 'mock' ? 'Mock / Stub' : p.sub }}</div>
        </div>
        <span v-if="i < parts.length - 1" class="i-carbon-arrow-right arrow" />
      </template>
    </div>
    <div class="caption"><span class="i-carbon-center-to-fit" /> {{ cfg.label }}</div>
  </div>
</template>

<style scoped>
.scope-stack { width: 100%; }
.row { display: flex; align-items: stretch; gap: 4px; }
.box {
  flex: 1;
  border-radius: 8px;
  padding: 8px 4px;
  text-align: center;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 1px;
  border: 2px solid transparent;
}
.ico { font-size: 24px; }
.lbl { font-weight: 700; font-size: 14px; }
.sub { font-size: 11px; }
.real { background: var(--uni-navy); color: #fff; }
.real .ico { filter: drop-shadow(0 0 1px #fff); }
.mock { background: #fff; border: 2px dashed var(--uni-gold); color: #8a7440; }
.off { background: #f0f2f6; color: #b3bccb; }
.off .ico { opacity: 0.35; filter: grayscale(1); }
.arrow { align-self: center; color: #b3bccb; font-size: 16px; flex: none; }
.caption { margin-top: 6px; font-size: 14px; color: var(--uni-text-muted); display: flex; align-items: center; gap: 6px; }
</style>
