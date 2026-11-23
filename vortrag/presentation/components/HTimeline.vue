<script setup lang="ts">
// Horizontale Zeitleiste: items mit year, title, text, icon (Iconify-Klasse)
const props = defineProps<{
  items: { year: string; title: string; text?: string; icon?: string; highlight?: boolean }[]
}>()
</script>

<template>
  <div class="tl">
    <div class="line" />
    <div v-for="it in props.items" :key="it.title" class="it" :class="{ hl: it.highlight }">
      <div class="year">{{ it.year }}</div>
      <div class="dot"><span v-if="it.icon" :class="it.icon" /></div>
      <div class="title">{{ it.title }}</div>
      <div v-if="it.text" class="text">{{ it.text }}</div>
    </div>
  </div>
</template>

<style scoped>
.tl { position: relative; display: flex; justify-content: space-between; gap: 10px; }
.line { position: absolute; left: 4%; right: 4%; top: 46px; height: 3px; background: var(--uni-blue-pale); }
.it { position: relative; flex: 1; display: flex; flex-direction: column; align-items: center; text-align: center; }
.year { font-weight: 700; font-size: 14px; color: var(--uni-gold); height: 20px; }
.dot {
  width: 46px;
  height: 46px;
  border-radius: 50%;
  background: #fff;
  border: 3px solid var(--uni-blue-pale);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  z-index: 1;
}
.hl .dot { border-color: var(--uni-navy); box-shadow: 0 0 0 4px #e3e7f0; }
.title { font-weight: 700; font-size: 15px; margin-top: 4px; }
.text { font-size: 12.5px; color: var(--uni-text-muted); line-height: 1.3; max-width: 160px; }
</style>
