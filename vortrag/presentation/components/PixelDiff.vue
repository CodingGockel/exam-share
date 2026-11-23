<script setup lang="ts">
// Visuelle Regression schematisch: Referenz, aktueller Stand, Pixel-Differenz.
// Im aktuellen Stand ist der Download-Button 4 px verrutscht und der Zähler anders eingefärbt.
</script>

<template>
  <div class="pixel-diff">
    <div v-for="v in ['baseline', 'actual', 'diff']" :key="v" class="panel">
      <div class="cap">
        <template v-if="v === 'baseline'"><span class="i-carbon-image-reference" /> Referenz (Baseline)</template>
        <template v-else-if="v === 'actual'"><span class="i-carbon-image" /> Aktueller Stand</template>
        <template v-else><span class="i-carbon-compare" /> Differenz</template>
      </div>
      <div class="screen" :class="v">
        <div v-for="n in 3" :key="n" class="item" :class="{ collapsed: n === 3 }">
          <div class="l1" />
          <div class="l2" />
          <div class="votes"><span class="vote" :class="{ shifted: v !== 'baseline' && n === 1 }" /><span class="vote" /></div>
          <div class="btn" :class="{ shifted: v !== 'baseline' && n === 2 }" />
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.pixel-diff { display: grid; grid-template-columns: repeat(3, 1fr); gap: 12px; }
.cap { font-size: 13px; font-weight: 700; display: flex; align-items: center; gap: 5px; margin-bottom: 4px; }
.screen { border: 2px solid #d0d5df; border-radius: 6px; padding: 8px; background: #fff; display: flex; flex-direction: column; gap: 6px; height: 150px; }
.item { position: relative; border-radius: 4px; background: #f5f6f9; padding: 6px 8px; height: 38px; }
.item.collapsed { opacity: 0.45; }
.l1 { width: 55%; height: 7px; border-radius: 3px; background: var(--uni-navy); }
.l2 { width: 75%; height: 5px; border-radius: 3px; background: var(--uni-blue-pale); margin-top: 5px; }
.votes { position: absolute; right: 54px; top: 12px; display: flex; gap: 4px; }
.vote { width: 14px; height: 12px; border-radius: 3px; background: var(--uni-gold); }
.btn { position: absolute; right: 8px; top: 11px; width: 36px; height: 14px; border-radius: 3px; background: var(--uni-navy); }
.actual .btn.shifted { top: 15px; right: 4px; }
.actual .vote.shifted { background: #e0951d; }

/* Differenz: alles Gleiche fast weiß, nur geänderte Pixel magenta */
.diff { background: #fafafa; }
.diff .item { background: #f7f7f7; }
.diff .l1, .diff .l2, .diff .vote, .diff .btn { background: #ececec; }
.diff .item.collapsed { opacity: 1; }
.diff .btn.shifted { background: #ff00c8; box-shadow: -4px -4px 0 0 #ff00c8; top: 15px; right: 4px; }
.diff .vote.shifted { background: #ff00c8; }
</style>
