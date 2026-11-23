<script setup lang="ts">
// Platzhalter für die Demo-Videos. Sobald das Video existiert:
//   <DemoVideo src="/videos/3-1-unit.mp4" title="…" />
// Videos gehören nach public/videos/.
const props = defineProps<{ src?: string; title: string; duration?: string; poster?: string }>()
</script>

<template>
  <div class="demo-video">
    <video v-if="props.src" :src="props.src" :poster="props.poster" controls preload="metadata" />
    <div v-else class="placeholder">
      <span class="i-carbon-play-filled-alt play" />
      <div class="label">Demo-Video folgt</div>
      <div class="title">{{ props.title }}</div>
      <div v-if="props.duration" class="duration"><span class="i-carbon-time" /> {{ props.duration }}</div>
    </div>
  </div>
</template>

<style scoped>
.demo-video { width: 100%; height: 100%; min-height: 150px; }
video { width: 100%; height: 100%; border-radius: 8px; background: #000; object-fit: contain; }
.placeholder {
  width: 100%;
  height: 100%;
  border: 2px dashed var(--uni-blue-pale);
  border-radius: 10px;
  background: repeating-linear-gradient(135deg, #f7f8fb 0 14px, #eef0f6 14px 28px);
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 4px;
  text-align: center;
  padding: 12px;
}
.play { font-size: 46px; color: var(--uni-navy); }
.label { font-size: 12px; font-weight: 700; letter-spacing: 0.1em; text-transform: uppercase; color: var(--uni-gold); }
.title { font-size: 17px; font-weight: 600; max-width: 90%; }
.duration { font-size: 13px; color: var(--uni-text-muted); display: inline-flex; gap: 4px; align-items: center; }
</style>
