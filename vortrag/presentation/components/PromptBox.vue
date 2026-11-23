<script setup lang="ts">
// Prompt an den Agenten, als Chat-Blase dargestellt.
const props = withDefaults(defineProps<{ label?: string }>(), { label: 'Prompt an den Agenten' })
</script>

<template>
  <div class="prompt-box">
    <div class="head"><span class="i-carbon-chat" /> {{ props.label }}</div>
    <div class="body"><slot /></div>
  </div>
</template>

<style scoped>
.prompt-box {
  border-radius: 10px 10px 10px 2px;
  background: var(--uni-navy);
  color: #fff;
  padding: 10px 14px;
  font-size: 15px;
  line-height: 1.4;
}
.head {
  font-size: 11px;
  font-weight: 700;
  letter-spacing: 0.08em;
  text-transform: uppercase;
  color: #f0dfaa;
  display: flex;
  gap: 5px;
  align-items: center;
  margin-bottom: 4px;
}
.body :deep(p) { margin: 3px 0; }
.body :deep(code) { background: rgba(255, 255, 255, 0.15); color: #fff; }
</style>
