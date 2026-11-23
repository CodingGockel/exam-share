<script setup lang="ts">
// Desktop- und Handy-Ansicht der Ergebnisliste (schematisch).
// issues: zeigt typische Layoutfehler, die ein Mensch bzw. die KI sofort sieht,
// ein funktionaler Test aber nicht (Überlauf, überlappende Buttons, Kontrast).
const props = defineProps<{ issues?: boolean }>()

const items = [
  { subject: 'Software Engineering', meta: 'WS24/25 · FSU Jena · Informatik (B.Sc.)', up: 12, down: 1 },
  { subject: 'Datenbanksysteme', meta: 'SS24 · FSU Jena · Informatik (B.Sc.)', up: 7, down: 2 },
  { subject: 'Analysis I', meta: 'WS23/24 · FSU Jena · Mathematik', up: 1, down: 6, collapsed: true },
]
</script>

<template>
  <div class="devices" :class="{ issues: props.issues }">
    <div class="desktop">
      <div class="bar"><i /><i /><i /><span>localhost/suchen</span></div>
      <div class="page">
        <div v-for="it in items" :key="it.subject" class="item" :class="{ collapsed: it.collapsed }">
          <div class="txt">
            <div class="s">{{ it.subject }}</div>
            <div class="m">{{ it.meta }}</div>
          </div>
          <div class="v">👍 {{ it.up }} 👎 {{ it.down }}</div>
          <div class="dl">PDF</div>
        </div>
      </div>
    </div>
    <div class="phone">
      <div class="notch" />
      <div class="page">
        <div v-for="it in items" :key="it.subject" class="item" :class="{ collapsed: it.collapsed }">
          <div class="s">{{ it.subject }}</div>
          <div class="m">{{ it.meta }}</div>
          <div class="row"><span class="v">👍 {{ it.up }} 👎 {{ it.down }}</span><span class="dl">PDF</span></div>
        </div>
      </div>
      <div v-if="props.issues" class="bug b1">Text läuft über</div>
      <div v-if="props.issues" class="bug b2">Buttons überlappen</div>
    </div>
  </div>
</template>

<style scoped>
.devices { display: flex; gap: 18px; align-items: flex-end; }
.desktop { flex: 1; border: 3px solid var(--uni-navy); border-radius: 8px; overflow: hidden; background: #fff; }
.bar { background: #e3e7f0; padding: 4px 8px; display: flex; align-items: center; gap: 4px; font-size: 10px; color: var(--uni-text-muted); }
.bar i { width: 7px; height: 7px; border-radius: 50%; background: #b3bccb; display: inline-block; }
.bar span { margin-left: 8px; background: #fff; border-radius: 3px; padding: 0 8px; }
.page { padding: 8px; display: flex; flex-direction: column; gap: 5px; }
.desktop .item { display: flex; align-items: center; gap: 8px; background: #f5f6f9; border-radius: 4px; padding: 5px 8px; }
.txt { flex: 1; }
.s { font-weight: 700; font-size: 12px; }
.m { font-size: 10px; color: var(--uni-text-muted); }
.v { font-size: 10.5px; white-space: nowrap; }
.dl { font-size: 10px; background: var(--uni-navy); color: #fff; border-radius: 3px; padding: 1px 6px; }
.collapsed { opacity: 0.45; }
.phone { position: relative; width: 135px; border: 3px solid var(--uni-navy); border-radius: 16px; background: #fff; padding: 12px 0 8px; flex: none; }
.notch { position: absolute; top: 3px; left: 50%; transform: translateX(-50%); width: 36px; height: 5px; border-radius: 3px; background: var(--uni-navy); }
.phone .page { padding: 6px; }
.phone .item { background: #f5f6f9; border-radius: 4px; padding: 4px 6px; }
.phone .s { font-size: 11px; }
.phone .m { font-size: 9px; }
.phone .row { display: flex; justify-content: space-between; align-items: center; margin-top: 2px; }

/* Fehlerbilder */
.issues .phone .item:first-child .m { white-space: nowrap; overflow: visible; color: var(--bad); }
.issues .phone .item:nth-child(2) .row { justify-content: flex-start; }
.issues .phone .item:nth-child(2) .dl { margin-left: -22px; outline: 2px solid var(--bad); }
.bug {
  position: absolute;
  left: 50%;
  transform: translateX(-50%);
  font-size: 11px;
  font-weight: 700;
  color: var(--bad);
  background: var(--bad-bg);
  border-radius: 4px;
  padding: 1px 6px;
  white-space: nowrap;
}
.b1 { top: -26px; }
.b2 { bottom: -26px; }
</style>
