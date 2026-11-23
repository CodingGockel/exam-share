---
layout: section
part: '3.4'
---

# UI-Tests: Aussehen

Funktioniert ≠ sieht richtig aus

::visual::
<TestPyramid :width="330" active="e2e" />

---
step: 1
topic: 3.4 UI-Look
---

# Sieht die Oberfläche richtig aus?

<div class="grid grid-cols-[1.35fr_1fr] gap-6 items-center">
<DeviceFrames issues class="my-6" />
<div>

- Alle funktionalen Tests **grün**, trotzdem kaputt:
  - Text läuft auf dem Handy über
  - Buttons überlappen
  - Kontrast zu schwach
- Aus der Spezifikation: eingeklappte Klausuren sind **ausgegraut**
- Prüfen: **Layout**, **Responsiveness**, **Darstellung**, **Barrierefreiheit**

</div>
</div>

<!--
Ein Playwright-Test, der auf data-testid klickt, merkt nicht, dass ein Button halb verdeckt ist
oder dass der Text über den Rand läuft.
-->

---
step: 2
topic: 3.4 UI-Look
---

# Klassisch: visuelle Regression per Screenshot-Diff

<PixelDiff />

<div class="grid grid-cols-[1.2fr_1fr] gap-5 mt-4">
<div>

<FlowSteps small :steps="[
  { title: 'Baseline', text: 'Screenshot freigeben', color: 'blue' },
  { title: 'Änderung', text: 'neuer Screenshot', color: 'navy' },
  { title: 'Vergleich', text: 'Pixel für Pixel', color: 'navy' },
  { title: 'Mensch', text: 'Bug oder neue Baseline?', color: 'gold' },
]" />

```ts
// Playwright (TypeScript) hat das eingebaut:
await expect(page).toHaveScreenshot('suche.png', { maxDiffPixelRatio: 0.01 })
```

</div>
<div class="small">

**Werkzeuge**: Playwright (TS), BackstopJS, Percy, Chromatic, Applitools (eigene „Visual AI“)

**Stolpersteine**: Schriftglättung, Animationen, Datum/Uhrzeit, dynamische Daten, unterschiedliche Rechner → **flaky**

</div>
</div>

<!--
Playwright for Java hat keinen eingebauten Screenshot-Vergleich; dort braucht man ein Zusatztool
oder einen eigenen Vergleich.
-->

---
step: 2
topic: 3.4 UI-Look
---

# Klassisch: Accessibility-Checks

<div class="grid grid-cols-[1.25fr_1fr] gap-6">
<div>

```java
// Playwright for Java + axe-core
AxeResults results = new AxeBuilder(page)
        .withTags(List.of("wcag2a", "wcag2aa"))
        .analyze();

assertThat(results.getViolations()).isEmpty();
```

<div class="flex gap-3 mt-3">
  <div class="stat"><div class="n">A</div><div class="l">WCAG-Stufe A</div></div>
  <div class="stat"><div class="n">AA</div><div class="l">üblicher Zielstandard</div></div>
  <div class="stat"><div class="n">AAA</div><div class="l">höchste Stufe</div></div>
</div>

</div>
<div class="flex flex-col gap-3">
<div class="card" style="background: var(--ok-bg)">
<h3 class="!text-[var(--ok)]"><span class="i-carbon-bot" /> Automatisch prüfbar</h3>
<div class="small">Kontrast · Alt-Texte · Labels an Formularfeldern · ARIA-Attribute · Überschriften-Hierarchie</div>
</div>
<div class="card" style="background: var(--warn-bg)">
<h3 class="!text-[#9a6410]"><span class="i-carbon-user" /> Braucht ein Urteil</h3>
<div class="small">Verständliche Texte · sinnvolle Reihenfolge · Bedienung per Tastatur im ganzen Ablauf · Erlebnis mit Screenreader</div>
</div>
<p class="small muted"><span class="i-carbon-information" /> Automatische Tools finden nur einen Teil der WCAG-Probleme. Hier kann die KI-Beurteilung ergänzen.</p>
</div>
</div>

<style>
.stat { flex: 1; text-align: center; background: var(--uni-gray); border-radius: 8px; padding: 6px; }
.stat .n { font-family: var(--font-serif); font-size: 26px; color: var(--uni-navy); }
.stat .l { font-size: 12px; color: var(--uni-text-muted); }
</style>

---
step: 3
topic: 3.4 UI-Look
---

# KI-Demo: Der Agent beurteilt Screenshots

<FlowSteps small :steps="[
  { title: 'Screenshots', text: 'Desktop 1280 px, Handy 390 px', icon: 'i-carbon-screen', color: 'blue' },
  { title: 'Modell', text: 'Bild + Spezifikation', icon: 'i-carbon-machine-learning-model', color: 'navy' },
  { title: 'Befunde', text: 'mit Begründung und Stelle', icon: 'i-carbon-list-checked', color: 'gold' },
  { title: 'Mensch', text: 'entscheidet: Bug oder ok?', icon: 'i-carbon-user', color: 'blue' },
]" />

<div class="grid grid-cols-[1fr_1.2fr] gap-5 mt-4">
<PromptBox>

Mach Screenshots der Ergebnisseite in Desktop- und Handybreite. Prüfe:
Sind eingeklappte Einträge **ausgegraut**? Läuft Text **über**?
**Überlappen** Elemente? Ist der **Kontrast** ausreichend?
Nenne zu jedem Befund die Stelle im Screenshot.

</PromptBox>
<DemoVideo title="Agent prüft Layout auf Desktop und Handy" duration="ca. 1–2 min" class="!min-h-[170px]" />
</div>

<!--
Der Agent hat kein Pixel-Orakel, sondern beurteilt das Bild wie ein Mensch, mit Bezug zur Spezifikation.
-->

---
step: 4
topic: 3.4 UI-Look
---

# Pixel-Diff vs. KI-Urteil

<div class="spectrum">
  <div class="axis"><span>streng, exakt</span><span>tolerant, versteht Bedeutung</span></div>
  <div class="bar-grad" />
  <div class="points">
    <div class="pt" style="left: 6%"><div class="dot" /><b>Pixel-Diff</b><span>meldet jede Änderung, auch irrelevante</span></div>
    <div class="pt" style="left: 50%"><div class="dot" /><b>Visual AI</b><span>ignoriert Rauschen, erkennt Layoutänderungen</span></div>
    <div class="pt" style="left: 94%"><div class="dot" /><b>LLM-Urteil</b><span>„sieht das richtig aus?“, kann übersehen oder halluzinieren</span></div>
  </div>
</div>

<ProsCons class="mt-2">
<template #pros>

- Versteht **Bedeutung**: „ausgegraut“, „überlappt“, „unleserlich“
- **Keine Baseline** nötig, prüft auch neue Seiten
- Bewertet gegen die **Spezifikation**, nicht gegen alte Pixel

</template>
<template #cons>

- **Unscharf**: kleine Verschiebungen fallen nicht auf
- **Nicht deterministisch**, Urteil kann schwanken
- Ersetzt keine Prüfung mit **echten Nutzer:innen**

</template>
</ProsCons>

<style>
.spectrum { position: relative; margin-bottom: 78px; }
.axis { display: flex; justify-content: space-between; font-size: 13px; font-weight: 700; color: var(--uni-text-muted); margin-bottom: 4px; }
.bar-grad { height: 8px; border-radius: 4px; background: linear-gradient(90deg, var(--uni-navy), var(--uni-blue-light), var(--uni-gold)); }
.points { position: absolute; left: 0; right: 0; top: 20px; }
.pt { position: absolute; transform: translateX(-50%); display: flex; flex-direction: column; align-items: center; text-align: center; width: 240px; font-size: 14px; line-height: 1.2; }
.pt:first-child { transform: translateX(-6%); align-items: flex-start; text-align: left; }
.pt:last-child { transform: translateX(-94%); align-items: flex-end; text-align: right; }
.pt span { font-size: 12px; color: var(--uni-text-muted); }
.dot { width: 16px; height: 16px; border-radius: 50%; background: #fff; border: 3px solid var(--uni-navy); margin: -4px 0 3px; }
</style>

---
step: 5
topic: 3.4 UI-Look
---

# Skill: `ui-visual-check`

<div class="grid grid-cols-[1.6fr_1fr] gap-5">

```markdown
---
name: ui-visual-check
description: Aussehen und Barrierefreiheit der Prüfprot-Oberfläche
  per Screenshot und axe-core prüfen
---
# Oberfläche visuell prüfen
1. Screenshots: Desktop 1280 px und Handy 390 px Breite
2. Prüfliste: Überlauf, Überlappung, Kontrast, Ausrichtung,
   eingeklappte Klausuren ausgegraut + "Trotzdem anzeigen"
3. Zu jedem Befund: Seite, Breite, Stelle, Begründung
4. Keine Befunde erfinden: unsicher → als Frage markieren
5. Accessibility: axe-core (wcag2a, wcag2aa) zusätzlich laufen
   lassen, Ergebnisse einordnen
6. Ergebnis ist ein Bericht, kein Test: Mensch entscheidet
```

<SkillCollection current="ui-visual-check" />
</div>
