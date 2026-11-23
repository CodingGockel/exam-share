---
layout: section
part: '3.5'
---

# Regressionstests und Bugs verifizieren

Behobene Fehler dürfen nicht zurückkommen

::visual::
<TestPyramid :width="330" active="regression" />

---
step: 1
topic: 3.5 Regression
---

# Was ist ein Regressionstest?

<FlowSteps :steps="[
  { title: 'Bug gemeldet', text: 'Suche findet zu viel', icon: 'i-carbon-debug', color: 'bad' },
  { title: 'Fix', text: 'Bug behoben, ausgeliefert', icon: 'i-carbon-checkmark-filled', color: 'ok' },
  { title: 'Monate später', text: 'Refactoring der Suche', icon: 'i-carbon-time', color: 'blue' },
  { title: 'Bug ist zurück', text: 'niemand merkt es', icon: 'i-carbon-debug', color: 'bad' },
]" />

<div class="grid grid-cols-[1fr_auto] gap-6 mt-5 items-center">
<div>

- Regression ist **keine Ebene**, sondern ein **Zweck**: quer über Unit, Integration und E2E
- Jede Fehlerbehebung hinterlässt einen **Test, der genau diesen Bug fängt**
- Die Regressionssuite läuft bei **jeder Änderung** (CI, Teil 5)

</div>
<TestPyramid :width="220" active="regression" />
</div>

<!--
Rückgriff auf die Unterscheidung Testebenen vs. Testarten aus Teil 1.
-->

---
step: 2
topic: 3.5 Regression
---

# Klassisch: erst der rote Test, dann der Fix

<FlowSteps :steps="[
  { title: 'Bug-Meldung', text: 'Schritte, erwartet, tatsächlich', icon: 'i-carbon-document', color: 'blue' },
  { title: 'Test schreiben', text: 'reproduziert den Bug', icon: 'i-carbon-code', color: 'navy' },
  { title: 'Test ist ROT', text: 'Bug bewiesen', icon: 'i-carbon-close-filled', color: 'bad' },
  { title: 'Fix', text: 'nur Produktivcode ändern', icon: 'i-carbon-tools', color: 'navy' },
  { title: 'Test ist GRÜN', text: 'bleibt als Regressionstest', icon: 'i-carbon-checkmark-filled', color: 'ok' },
]" />

<div class="grid grid-cols-2 gap-5 mt-5">
<div class="card">
<h3><span class="i-carbon-warning-alt gold" /> Warum zuerst rot?</h3>
<div class="small">Ein Test, der <b>vor</b> dem Fix nicht rot war, beweist nichts. Vielleicht testet er am Bug vorbei.</div>
</div>
<div class="card">
<h3><span class="i-carbon-renew gold" /> Verbindung zu TDD</h3>
<div class="small">Dasselbe Prinzip wie Red → Green → Refactor aus Teil 1, nur ausgelöst durch einen Bug statt durch ein neues Feature.</div>
</div>
</div>

---
step: 2
topic: 3.5 Regression
---

# Characterization Tests (Golden Master)

<div class="gm">
  <div class="gm-row">
    <div class="gm-box in"><span class="i-carbon-data-1" /> viele Eingaben<br><span class="tiny">z. B. alte CSV-Dateien</span></div>
    <span class="i-carbon-arrow-right arr" />
    <div class="gm-box code"><span class="i-carbon-code" /> bestehender Code<br><span class="tiny">vor dem Refactoring</span></div>
    <span class="i-carbon-arrow-right arr" />
    <div class="gm-box gold-box"><span class="i-carbon-document-tasks" /> Ausgaben aufzeichnen<br><span class="tiny">„Golden Master“</span></div>
  </div>
  <div class="gm-row">
    <div class="gm-box in"><span class="i-carbon-data-1" /> dieselben Eingaben</div>
    <span class="i-carbon-arrow-right arr" />
    <div class="gm-box code"><span class="i-carbon-code" /> neuer Code<br><span class="tiny">nach dem Refactoring</span></div>
    <span class="i-carbon-arrow-right arr" />
    <div class="gm-box cmp"><span class="i-carbon-compare" /> Vergleich mit Golden Master<br><span class="tiny">gleich → Verhalten unverändert</span></div>
  </div>
</div>

<div class="grid grid-cols-2 gap-5 mt-4">
<div>

- Hält fest, was der Code **tut**, nicht was er tun **soll**
- Ideal vor Refactorings an Code **ohne Tests**
- Beispiel: CSV-Import im Modul `legacy/`

</div>
<div>

- Auch **Bugs werden eingefroren**, das ist gewollt
- KI kann Eingaben erzeugen und den Vergleich bauen
- Danach Schritt für Schritt durch echte Tests ersetzen

</div>
</div>

<style>
.gm { display: flex; flex-direction: column; gap: 8px; }
.gm-row { display: flex; align-items: stretch; gap: 8px; }
.gm-box { flex: 1; border-radius: 8px; padding: 8px 12px; font-size: 15px; font-weight: 600; line-height: 1.25; }
.gm-box .tiny { font-weight: 400; }
.in { background: var(--uni-gray); }
.code { background: var(--uni-navy); color: #fff; }
.gold-box { background: var(--uni-gold); color: #fff; }
.cmp { background: var(--ok-bg); color: var(--ok); border: 2px solid var(--ok); }
.arr { align-self: center; color: var(--uni-gold); font-size: 22px; flex: none; }
</style>

---
step: 3
topic: 3.5 Regression
---

# KI-Demo: Bug → roter Test → Fix

<div class="grid grid-cols-[1fr_1.25fr] gap-5 h-full">
<div class="flex flex-col gap-3">

<PromptBox label="Bug-Meldung + Prompt">

**Bug:** Die Suche nach `100%` im Modul findet alle Klausuren.

Schreib **zuerst einen Test**, der den Bug reproduziert, und zeig mir,
dass er **rot** ist. Ändere danach nur den Produktivcode, bis der Test grün ist.

</PromptBox>

<div class="small">

**Worauf achten im Video?**

- Ist der Test vor dem Fix **wirklich rot**?
- Auf welcher **Ebene** reproduziert die KI den Bug?
- Wird beim Fix die **Testdatei** angefasst?

</div>
</div>
<DemoVideo title="Bugfix mit reproduzierendem Test" duration="ca. 2 min" />
</div>

<!--
Beispiel-Bugmeldung anpassen, sobald das Video aufgenommen ist.
Laut Spezifikation dürfen % und _ in der Suche nicht als Wildcard wirken.
-->

---
step: 3
topic: 3.5 Regression
---

# Leitplanken: damit die KI nicht „schummelt“

<div class="grid grid-cols-[1fr_1.1fr] gap-6">
<div>
<div class="fork">
  <div class="start"><span class="i-carbon-close-filled" /> Test ist rot</div>
  <div class="branches">
    <div class="branch good"><span class="i-carbon-checkmark-filled" /><b>Produktivcode fixen</b><span>der Bug ist weg</span></div>
    <div class="branch bad-b"><span class="i-carbon-close-filled" /><b>Assertion anpassen, <code>@Disabled</code>, Sonderfall im Code</b><span>Test grün, Bug noch da</span></div>
  </div>
</div>
</div>
<div class="flex flex-col gap-2">
  <div class="card flex gap-3 items-start"><span class="icon-circle"><span class="i-carbon-document" /></span><div class="small"><b>CLAUDE.md</b><br>„Fehlschlagender Test = möglicher Bug: melden, nicht die Assertion anpassen.“</div></div>
  <div class="card flex gap-3 items-start"><span class="icon-circle"><span class="i-carbon-locked" /></span><div class="small"><b>Hook sperrt Testdateien</b> während des Fixes</div></div>
  <div class="card flex gap-3 items-start"><span class="icon-circle"><span class="i-carbon-play" /></span><div class="small"><b>Hook führt Tests aus</b> nach jeder Änderung</div></div>
</div>
</div>

```json
{ "hooks": { "PreToolUse": [{ "matcher": "Edit|Write",
    "hooks": [{ "type": "command", "command": ".claude/hooks/block-test-edits.sh" }] }] } }
```

<style>
.fork { display: flex; flex-direction: column; align-items: stretch; gap: 8px; }
.start { align-self: center; background: var(--bad); color: #fff; font-weight: 700; border-radius: 20px; padding: 5px 16px; display: flex; gap: 6px; align-items: center; }
.branches { display: grid; grid-template-columns: 1fr 1fr; gap: 10px; }
.branch { border-radius: 8px; padding: 10px; display: flex; flex-direction: column; gap: 3px; font-size: 14px; line-height: 1.25; }
.branch span:last-child { font-size: 12px; }
.good { background: var(--ok-bg); color: var(--ok); border: 2px solid var(--ok); }
.bad-b { background: var(--bad-bg); color: var(--bad); border: 2px dashed var(--bad); }
</style>

<!--
Ausblick auf Teil 5 (typische Fehler der KI, Hooks als Leitplanke).
Der Hook wird in .claude/settings.json eingetragen; das Skript blockiert Änderungen an *Test.java.
-->

---
step: 4
topic: 3.5 Regression
---

# Bewertung: Regression und Bugfixes mit KI

<ProsCons>
<template #pros>

- Macht aus einer Bug-Meldung schnell einen **reproduzierenden Test**
- Wählt oft eine **passende Ebene** (Unit statt E2E)
- Baut **Golden-Master-Tests** für Code ohne Tests
- Jeder Fix hinterlässt einen **Regressionstest**

</template>
<template #cons>

- Will den Test **grün** bekommen → Risiko: Test statt Code ändern
- Behauptet „Test ist rot“, ohne ihn **ausgeführt** zu haben
- Test reproduziert ein **anderes** Problem als gemeldet
- Golden Master friert auch **Bugs** ein

</template>
<template #note>
<div class="takeaway"><span class="i-carbon-view" /> Beim Review immer prüfen: <strong>War der Test vor dem Fix rot?</strong> Wurde die <strong>Testdatei</strong> beim Fix geändert?</div>
</template>
</ProsCons>

---
step: 5
topic: 3.5 Regression
---

# Skill: `regression-bugfix`

<div class="grid grid-cols-[1.6fr_1fr] gap-5">

```markdown
---
name: regression-bugfix
description: Bugs in Prüfprot verifizieren und beheben,
  immer mit reproduzierendem Test zuerst
---
# Bug beheben
1. Bug-Meldung lesen: Schritte, erwartet, tatsächlich
2. Niedrigste passende Ebene wählen (Unit vor Integration
   vor E2E)
3. Test schreiben und AUSFÜHREN: Er muss rot sein
   (Ausgabe zeigen)
4. Fix nur im Produktivcode, Testdatei bleibt unverändert
5. Alle Tests ausführen: ./gradlew build
6. Test bleibt als Regressionstest, Name beschreibt den Bug
7. Kein @Disabled, keine abgeschwächten Assertions
```

<SkillCollection current="regression-bugfix" />
</div>

---

# Zwischenfazit Teil 3

<div class="grid grid-cols-[260px_1fr] gap-5 items-center">
<TestPyramid :width="260" />
<div>

| Ebene | KI stark bei … | Achtung bei … |
|---|---|---|
| **Unit** | vielen Fällen, Parametrisierung, Testdaten | Implementierung spiegeln, Grenzwerte |
| **Integration** | Boilerplate, Statuscode-Matrix, curl | H2/Mocks, Nebenläufigkeit |
| **UI-Funktion** | Playwright-Tests, Erkunden im Browser | Sleeps, Determinismus, Kosten |
| **UI-Aussehen** | Bedeutung erkennen, keine Baseline | unscharf, halluzinierte Befunde |
| **Regression** | Bug → roter Test | Test statt Code ändern |

</div>
</div>

<div class="takeaway mt-4">Auf jeder Ebene dieselbe Frage: <strong>Prüft der Test die Anforderung oder nur das, was der Code gerade tut?</strong></div>

<!--
Überleitung zu Teil 4 (nicht-funktionales Testen) bzw. Teil 5 (Qualität der KI-Tests).
-->
