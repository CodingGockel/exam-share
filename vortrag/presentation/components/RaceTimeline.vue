<script setup lang="ts">
// Zwei gleichzeitige Votes auf dieselbe Klausur.
// mode 'lost': Read-Modify-Write in Java → ein Vote geht verloren
// mode 'atomic': UPDATE … SET upvotes = upvotes + 1 → beide zählen
const props = defineProps<{ mode: 'lost' | 'atomic' }>()

const lost = [
  { a: 'SELECT upvotes → 5', b: '', db: 5 },
  { a: '', b: 'SELECT upvotes → 5', db: 5 },
  { a: 'Java: 5 + 1 = 6', b: 'Java: 5 + 1 = 6', db: 5 },
  { a: 'UPDATE … = 6', b: '', db: 6 },
  { a: '', b: 'UPDATE … = 6', db: 6 },
]
const atomic = [
  { a: 'UPDATE … = upvotes + 1', b: '', db: 6 },
  { a: '', b: 'UPDATE … + 1 (wartet auf Sperre)', db: 7 },
]
const rows = props.mode === 'lost' ? lost : atomic
</script>

<template>
  <div class="race" :class="props.mode">
    <div class="hdr">
      <div>Request A 👍</div>
      <div>Request B 👍</div>
      <div>DB: upvotes</div>
    </div>
    <div v-for="(r, i) in rows" :key="i" class="row">
      <div class="cell" :class="{ empty: !r.a }">{{ r.a }}</div>
      <div class="cell" :class="{ empty: !r.b }">{{ r.b }}</div>
      <div class="db">{{ r.db }}</div>
    </div>
    <div class="result">
      <template v-if="props.mode === 'lost'">
        <span class="i-carbon-close-filled" /> Ergebnis 6 statt 7: ein Vote ist verloren
      </template>
      <template v-else>
        <span class="i-carbon-checkmark-filled" /> Ergebnis 7: Postgres serialisiert die Updates
      </template>
    </div>
  </div>
</template>

<style scoped>
.race { font-size: 12.5px; width: 100%; }
.hdr, .row { display: grid; grid-template-columns: 1fr 1fr 70px; gap: 4px; }
.hdr { font-weight: 700; font-size: 12px; color: var(--uni-text-muted); margin-bottom: 2px; }
.row { margin: 2px 0; }
.cell {
  font-family: var(--font-mono);
  font-size: 11.5px;
  padding: 3px 6px;
  border-radius: 4px;
  background: #e3e7f0;
}
.cell.empty { background: transparent; border-left: 2px dotted #d0d5df; }
.db { text-align: center; font-weight: 700; background: #f5f6f9; border-radius: 4px; padding: 3px 0; }
.result { margin-top: 6px; font-weight: 700; display: flex; align-items: center; gap: 6px; padding: 5px 8px; border-radius: 6px; }
.lost .result { background: var(--bad-bg); color: var(--bad); }
.atomic .result { background: var(--ok-bg); color: var(--ok); }
</style>
