<script setup lang="ts">
// Kapiteltrenner im Stil der Titelfolie (die Vorlage hat dafür kein eigenes Layout).
// Frontmatter: part (z. B. "Teil 3" oder "3.1"), Titel = h1 im Slot, Untertitel = p.
// Der Slot "visual" (::visual::) landet rechts oben, z. B. die Testpyramide.
defineProps<{ part?: string }>()
</script>

<template>
  <div class="slidev-layout uni-section">
    <img class="logo" src="/img/logo.png" alt="Friedrich-Schiller-Universität Jena" />
    <div class="visual"><slot name="visual" /></div>
    <div class="gray-area" />
    <div class="bar-left" />
    <img class="bar-right" src="/img/farbstreifen.png" alt="" />

    <div class="text">
      <div v-if="part" class="part">{{ part }}</div>
      <slot />
    </div>
  </div>
</template>

<style scoped>
.uni-section { overflow: hidden; }
.logo { position: absolute; left: 4.3%; top: 8.7%; width: 17.2%; }
.visual {
  position: absolute;
  right: 5.1%;
  top: 5%;
  height: 50%;
  width: 55%;
  display: flex;
  justify-content: flex-end;
  align-items: center;
}
.gray-area { position: absolute; left: 0; right: 0; top: 58.4%; bottom: 0; background: var(--uni-gray); }
.bar-left { position: absolute; left: 0; width: 50.2%; top: 57.7%; height: 1.5%; background: var(--uni-navy-80); }
.bar-right { position: absolute; left: 50%; width: 50%; top: 57.7%; height: 1.5%; object-fit: fill; }
.text { position: absolute; left: 5.1%; right: 5.1%; top: 64%; }
.part {
  font-size: 18px;
  font-weight: 700;
  letter-spacing: 0.08em;
  text-transform: uppercase;
  color: var(--uni-gold);
}
.text :deep(h1) {
  font-family: var(--font-serif);
  font-size: 42px;
  font-weight: 400;
  margin: 4px 0 6px;
  line-height: 1.15;
}
.text :deep(p) { margin: 0; font-size: 20px; color: var(--uni-blue); }
</style>
