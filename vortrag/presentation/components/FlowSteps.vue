<script setup lang="ts">
// Horizontaler Ablauf mit Pfeilen. color: 'navy' | 'gold' | 'blue' | 'ok' | 'bad' | 'gray'
const props = defineProps<{
  steps: { title: string; text?: string; icon?: string; color?: string }[]
  small?: boolean
}>()
</script>

<template>
  <div class="flow" :class="{ small: props.small }">
    <template v-for="(s, i) in props.steps" :key="s.title">
      <div class="step" :class="s.color ?? 'navy'">
        <span v-if="s.icon" :class="s.icon" class="ico" />
        <div class="t">{{ s.title }}</div>
        <div v-if="s.text" class="x">{{ s.text }}</div>
      </div>
      <span v-if="i < props.steps.length - 1" class="i-carbon-arrow-right arrow" />
    </template>
  </div>
</template>

<style scoped>
.flow { display: flex; align-items: stretch; gap: 6px; width: 100%; }
.step {
  flex: 1;
  border-radius: 8px;
  padding: 10px 8px;
  text-align: center;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 2px;
  color: #fff;
}
.ico { font-size: 26px; }
.t { font-weight: 700; font-size: 16px; line-height: 1.2; }
.x { font-size: 12.5px; opacity: 0.92; line-height: 1.3; }
.small .ico { font-size: 20px; }
.small .t { font-size: 14px; }
.small .x { font-size: 11.5px; }
.arrow { align-self: center; flex: none; color: var(--uni-gold); font-size: 22px; }
.navy { background: var(--uni-navy); }
.gold { background: var(--uni-gold); }
.blue { background: var(--uni-blue); }
.ok { background: var(--ok); }
.bad { background: var(--bad); }
.gray { background: #eef0f5; color: var(--uni-navy); }
</style>
