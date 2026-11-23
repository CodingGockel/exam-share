---
theme: none
title: Software Testing mit KI
# Diese Angaben erscheinen in der Fußzeile jeder Inhaltsfolie (siehe layouts/default.vue)
author: Vorname Nachname & Vorname Nachname
event: Seminar Titel der Veranstaltung
date: TT.MM.2026
colorSchema: light
canvasWidth: 960
aspectRatio: 16/9
transition: fade
mdc: true
lineNumbers: false
fonts:
  provider: none
  sans: Roboto Condensed Variable
  serif: Roboto Serif Variable
  mono: Roboto Mono
exportFilename: vortrag
layout: cover
subtitle: Seminarvortrag · Vorname Nachname & Vorname Nachname
---

# Software Testing mit KI

Wer testet eigentlich die Tests, die die KI schreibt?

<!--
Titelfolie: Namen, Veranstaltung und Datum oben im Headmatter von slides.md anpassen
(author, event, date, subtitle).
-->

---

# Gliederung

<div class="grid grid-cols-3 gap-4 mt-1">
  <div class="agenda-card">
    <div class="num">1</div>
    <div>
      <h3>Grundlagen</h3>
      <ul class="small">
        <li>Was ist Testen, wozu?</li>
        <li>Testpyramide</li>
        <li>Prinzipien und Methoden</li>
      </ul>
    </div>
  </div>
  <div class="agenda-card">
    <div class="num">2</div>
    <div>
      <h3>KI im Testing</h3>
      <ul class="small">
        <li>Wie ein Agent testet</li>
        <li>Skills: Testwissen festhalten</li>
      </ul>
    </div>
  </div>
  <div class="agenda-card">
    <div class="num">3</div>
    <div>
      <h3>Durch die Ebenen</h3>
      <ul class="small">
        <li>Unit, Integration/API</li>
        <li>UI: Funktion und Aussehen</li>
        <li>Regression, Bugs verifizieren</li>
      </ul>
    </div>
  </div>
  <div class="agenda-card">
    <div class="num">4</div>
    <div>
      <h3>Nicht-funktional</h3>
      <ul class="small">
        <li>Security Testing</li>
        <li>Last- und Stresstests</li>
      </ul>
    </div>
  </div>
  <div class="agenda-card">
    <div class="num">5</div>
    <div>
      <h3>Qualität und Automatisierung</h3>
      <ul class="small">
        <li>Typische Fehler der KI</li>
        <li>Mehrere Agenten, CI/CD</li>
        <li>Testmigration (optional)</li>
      </ul>
    </div>
  </div>
  <div class="agenda-card">
    <div class="num">6</div>
    <div>
      <h3>Grenzen und Fazit</h3>
      <ul class="small">
        <li>Echte Testumgebungen</li>
        <li>Fazit und Ausblick</li>
      </ul>
    </div>
  </div>
</div>

<style>
.agenda-card { display: flex; gap: 12px; background: var(--uni-gray); border-radius: 8px; padding: 12px 14px; min-height: 128px; }
.agenda-card .num {
  flex: none; width: 38px; height: 38px; border-radius: 50%;
  background: var(--uni-navy); color: #fff; font-family: var(--font-serif);
  font-size: 20px; display: flex; align-items: center; justify-content: center;
}
.agenda-card h3 { margin: 4px 0 4px; font-size: 18px; }
</style>

---

# Roter Faden: Aufbau jedes Themas

<FlowSteps class="mt-2" :steps="[
  { title: 'Was & Wozu', text: 'Allgemein erklärt, ohne KI', icon: 'i-carbon-help', color: 'navy' },
  { title: 'Klassisch', text: 'Typische Tools und Methode', icon: 'i-carbon-tool-box', color: 'blue' },
  { title: 'KI-Demo', text: 'Video an der Demo-App Prüfprot', icon: 'i-carbon-play-filled', color: 'gold' },
  { title: 'Bewertung', text: 'Was kann die KI, wo sind Grenzen?', icon: 'i-carbon-scales', color: 'blue' },
  { title: 'Skill', text: 'Was wir als Skill festhalten', icon: 'i-carbon-skill-level', color: 'navy' },
]" />

<div class="grid grid-cols-[1fr_auto] gap-6 mt-6 items-center">
<div>

- Jedes Thema folgt **demselben Schema**
- Die Leiste oben rechts zeigt, wo wir gerade sind:

<TopicSteps :step="3" topic="Beispiel" class="mt-2" />

- Am Ende jedes Themas wächst die **Skill-Sammlung** um einen Eintrag

</div>
<SkillCollection current="unit-testing" class="w-60" />
</div>

<!--
Kurz erklären, dass jedes Kapitel gleich aufgebaut ist, damit das Publikum sich orientieren kann.
Die Skill-Sammlung ist der rote Faden und wird in 2.2 eingeführt.
-->

---
src: ./pages/03-0-teil3.md
---

---
src: ./pages/03-1-unit.md
---

---
src: ./pages/03-2-integration.md
---

---
src: ./pages/03-3-ui-funktion.md
---

---
src: ./pages/03-4-ui-aussehen.md
---

---
src: ./pages/03-5-regression.md
---

---
layout: end
---

# Vielen Dank für Ihre Aufmerksamkeit!
