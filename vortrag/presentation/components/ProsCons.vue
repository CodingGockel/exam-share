<script setup lang="ts">
// Vor- und Nachteile zweispaltig. Inhalt per Slots #pros und #cons (Markdown-Listen,
// mit Leerzeilen um den Inhalt). Optionaler Slot #note für eine Zeile darunter.
const props = withDefaults(defineProps<{ prosTitle?: string; consTitle?: string }>(), {
  prosTitle: 'Was die KI gut kann',
  consTitle: 'Grenzen und Risiken',
})
</script>

<template>
  <div>
  <div class="pros-cons">
    <div class="col pros">
      <div class="head"><span class="i-carbon-checkmark-filled" /> {{ props.prosTitle }}</div>
      <slot name="pros" />
    </div>
    <div class="col cons">
      <div class="head"><span class="i-carbon-warning-alt-filled" /> {{ props.consTitle }}</div>
      <slot name="cons" />
    </div>
  </div>
  <div v-if="$slots.note" class="note"><slot name="note" /></div>
  </div>
</template>

<style scoped>
.pros-cons { display: grid; grid-template-columns: 1fr 1fr; gap: 18px; font-size: 16px; }
.col { border-radius: 8px; padding: 10px 14px; }
.pros { background: var(--ok-bg); }
.cons { background: var(--bad-bg); }
.head { display: flex; align-items: center; gap: 6px; font-weight: 700; font-size: 17px; margin-bottom: 4px; }
.pros .head { color: var(--ok); }
.cons .head { color: var(--bad); }
.pros :deep(li)::before { background: var(--ok) !important; }
.cons :deep(li)::before { background: var(--bad) !important; }
.note { margin-top: 12px; }
</style>
