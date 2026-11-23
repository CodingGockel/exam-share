---
layout: section
part: '3.3'
---

# UI-Tests: Funktionalität

Funktioniert die Oberfläche so, wie Nutzer:innen sie bedienen?

::visual::
<TestPyramid :width="330" :active="['component', 'e2e']" />

---
step: 1
topic: 3.3 UI
---

# Was prüfen UI-Tests?

<FlowSteps small :steps="[
  { title: 'Startseite', text: 'Klausur suchen', icon: 'i-carbon-home', color: 'blue' },
  { title: 'Suchformular', text: 'Uni + Modul (Pflicht)', icon: 'i-carbon-search', color: 'blue' },
  { title: 'Ergebnisliste', text: 'Filter stehen in der URL', icon: 'i-carbon-list', color: 'navy' },
  { title: 'Abstimmen', text: '👍 / 👎, zweites Mal → Fehler', icon: 'i-carbon-thumbs-up', color: 'navy' },
  { title: 'Download', text: 'PDF herunterladen', icon: 'i-carbon-download', color: 'gold' },
]" />

<div class="mt-4">
<ScopeStack scope="e2e" />
</div>

<div class="grid grid-cols-2 gap-5 mt-3">
<div>

- Prüfen **Wege durch die App** (User Journeys)
- Alles läuft echt: Browser, Proxy, Backend, DB

</div>
<div>

- **Langsam und teuer** → nur die wichtigsten Wege
- Darunter: **Komponententests** fürs Frontend

</div>
</div>

<!--
E2E-Tests bilden ab, was Nutzer:innen tun. Sie sind am aussagekräftigsten, aber auch am langsamsten
und am anfälligsten. Deshalb wenige, gut gewählte Wege.
-->

---
step: 2
topic: 3.3 UI
---

# Klassisch: von Selenium zu Playwright

<HTimeline class="mt-4" :items="[
  { year: '2004', title: 'Selenium', text: 'Browser fernsteuern, später WebDriver-Standard', icon: 'i-logos-selenium' },
  { year: '2017', title: 'Puppeteer', text: 'Chrome über DevTools-Protokoll', icon: 'i-logos-puppeteer' },
  { year: '2017', title: 'Cypress', text: 'läuft im Browser, gute Entwicklererfahrung', icon: 'i-logos-cypress-icon' },
  { year: '2020', title: 'Playwright', text: 'Chromium, Firefox, WebKit; Java, TS, Python', icon: 'i-logos-playwright', highlight: true },
  { year: '2025', title: 'Browser-Agenten', text: 'LLM bedient den Browser, z. B. über MCP', icon: 'i-carbon-bot', highlight: true },
]" />

<div class="grid grid-cols-3 gap-4 mt-6">
  <div class="card flex gap-3 items-start"><span class="icon-circle"><span class="i-carbon-time" /></span><div class="small"><b>Auto-Waiting</b><br>wartet, bis Elemente bereit sind, statt fester Sleeps</div></div>
  <div class="card flex gap-3 items-start"><span class="icon-circle"><span class="i-carbon-checkmark-outline" /></span><div class="small"><b>Web-first Assertions</b><br><code>hasText(…)</code> prüft wiederholt, bis es passt</div></div>
  <div class="card flex gap-3 items-start"><span class="icon-circle"><span class="i-carbon-recording" /></span><div class="small"><b>Trace Viewer</b><br>Screenshots, DOM und Netzwerk jedes Schritts</div></div>
</div>

<!--
Kurzer historischer Bogen. In Prüfprot nutzen wir Playwright for Java im Modul e2e/.
-->

---
step: 2
topic: 3.3 UI
---

# Klassisch: Playwright-Test mit `data-testid`

<div class="grid grid-cols-[1.4fr_1fr] gap-5">

```java
@Test
void secondVoteInSameBrowserShowsError() {
    page.navigate(baseUrl + "/suchen?university=Jena&subject=Analysis");
    Locator exam = page.getByTestId("exam-item").first();

    exam.getByTestId("vote-up").click();
    assertThat(exam.getByTestId("upvote-count")).hasText("1");

    exam.getByTestId("vote-up").click();
    assertThat(exam.getByTestId("vote-error")).isVisible();
}
```

<div>
<h3>Selektoren: von fragil zu stabil</h3>
<div class="ladder">
  <div class="rung r1"><code>div &gt; ul li:nth-child(2) button</code><span>bricht bei jedem Umbau</span></div>
  <div class="rung r2"><code>getByText("👍")</code><span>bricht bei Textänderung</span></div>
  <div class="rung r3"><code>getByRole(BUTTON, …)</code><span>barrierefrei und robust</span></div>
  <div class="rung r4"><code>getByTestId("vote-up")</code><span>explizit für Tests</span></div>
</div>
<p class="small muted mt-2">Laut Spezifikation haben <b>alle</b> interaktiven Elemente ein <code>data-testid</code>.</p>
</div>
</div>

<style>
.ladder { display: flex; flex-direction: column; gap: 5px; }
.rung { display: flex; flex-direction: column; border-radius: 6px; padding: 4px 10px; font-size: 13px; }
.rung code { background: transparent !important; padding: 0 !important; font-size: 12.5px; }
.rung span { font-size: 11.5px; }
.r1 { background: var(--bad-bg); color: var(--bad); }
.r2 { background: var(--warn-bg); color: #9a6410; }
.r3 { background: #e3f0ea; color: #2f7a4f; }
.r4 { background: var(--ok-bg); color: var(--ok); border: 2px solid var(--ok); }
</style>

<!--
Der Test setzt eine frische Klausur ohne Stimmen voraus (Testdaten!).
Selektoren über data-testid entkoppeln Tests vom Layout.
-->

---
step: 2
topic: 3.3 UI
---

# Komponententests: die günstigere Ebene darunter

<div class="grid grid-cols-2 gap-5">
<div class="code-sm min-w-0">

```ts
// Vitest + Vue Test Utils: ohne Browser, ohne Backend
const download = '[data-testid="exam-download"]'

it('zeigt eingeklappte Klausur erst nach Klick',
  async () => {
    const wrapper = mount(ExamItem, {
      props: { exam: { ...exam, collapsed: true } },
    })
    expect(wrapper.find(download).exists()).toBe(false)

    await wrapper
      .get('[data-testid="exam-expand"]')
      .trigger('click')
    expect(wrapper.find(download).exists()).toBe(true)
  })
```

</div>
<div class="min-w-0">
<div class="code-sm">

```ts
// reine Logik: Semester-Dropdown
it('Januar gehört zum laufenden WS', () => {
  const opts = buildSemesterOptions(
    new Date(2026, 0, 15))
  expect(opts[0]).toBe('WS25/26')
  expect(opts.at(-1)).toBe('SS00')
})
```

</div>

| | Komponente | E2E |
|---|---|---|
| Laufzeit | Millisekunden | Sekunden |
| Umgebung | jsdom | echter Browser + Stack |
| Fehlerort | eine Komponente | irgendwo im Stack |

</div>
</div>

<!--
Grenzwerte gibt es auch im Frontend: Monatsgrenzen März/April und September/Oktober beim Semester-Dropdown.
Komponententests laufen über ./gradlew test (Vitest über das node-gradle-Plugin).
-->

---
step: 3
topic: 3.3 UI
---

# KI-Demo A: Die KI schreibt Playwright-Tests

<div class="grid grid-cols-[1fr_1.25fr] gap-5 h-full">
<div class="flex flex-col gap-3">

<PromptBox>

Schreib Playwright-Tests (Java) für Suchen, Hochladen und Abstimmen.
Nutze **nur `data-testid`-Selektoren**. Die Tests laufen gegen den
gestarteten Stack: `./gradlew stackUp e2eTest`.

</PromptBox>

<div class="small">

**Worauf achten im Video?**

- Woher kennt die KI die `data-testid`s?
- Wie sorgt sie für **Testdaten** (frische Klausur)?
- Nutzt sie Auto-Waiting oder baut sie **Sleeps** ein?
- Ergebnis: deterministisch, **in CI wiederholbar**

</div>
</div>
<DemoVideo title="KI schreibt Playwright-Tests" duration="ca. 1–2 min" />
</div>

---
step: 3
topic: 3.3 UI
---

# KI-Demo B: Der Agent bedient die App selbst

<div class="grid grid-cols-[auto_1fr] gap-6 items-center">
<CycleDiagram class="ml-12" :size="280" center="Browser-MCP" :steps="[
  { title: 'Seite lesen', icon: 'i-carbon-camera', color: 'navy' },
  { title: 'Plan', icon: 'i-carbon-machine-learning-model', color: 'gold' },
  { title: 'Klicken / Tippen', icon: 'i-carbon-touch-1', color: 'navy' },
  { title: 'Ergebnis prüfen', icon: 'i-carbon-view', color: 'blue' },
]" />
<div class="flex flex-col gap-3">
<PromptBox>

Öffne `http://localhost`, lade eine Klausur hoch, suche sie wieder und
stimme zweimal ab. Prüfe nach jedem Schritt mit einem **Screenshot**,
ob die App sich laut `CLAUDE.md` richtig verhält.

</PromptBox>
<DemoVideo title="Agent klickt sich per Browser-MCP durch die App" duration="ca. 1–2 min" class="!min-h-[150px]" />
</div>
</div>

<!--
Kein Testcode: Der Agent sieht Screenshots oder den Accessibility-Tree, entscheidet den nächsten Schritt
und prüft das Ergebnis. Flexibel, aber nicht deterministisch.
-->

---
step: 3
topic: 3.3 UI
---

# Exploratives Testen: die App kaputtmachen

<PromptBox label="Prompt an den Agenten">

Du bist ein kritischer Tester. Versuch, Prüfprot kaputtzumachen. Halte jeden Befund mit Schritten, erwartetem und tatsächlichem Verhalten fest.

</PromptBox>

<div class="grid grid-cols-3 gap-3 mt-4">
  <div class="card flex gap-3 items-start"><span class="icon-circle gold"><span class="i-carbon-document-pdf" /></span><div class="small"><b>Grenzen beim Upload</b><br>genau 10 MB, 10 MB + 1 Byte, leere Datei</div></div>
  <div class="card flex gap-3 items-start"><span class="icon-circle gold"><span class="i-carbon-security" /></span><div class="small"><b>Getarnte Dateien</b><br><code>.exe</code> als <code>.pdf</code>, Dateiname <code>../../etc/passwd.pdf</code></div></div>
  <div class="card flex gap-3 items-start"><span class="icon-circle gold"><span class="i-carbon-thumbs-up" /></span><div class="small"><b>Vote-Sperre</b><br>Doppelklick, zweiter Tab, Cookie löschen</div></div>
  <div class="card flex gap-3 items-start"><span class="icon-circle gold"><span class="i-carbon-search" /></span><div class="small"><b>Suche</b><br><code>%</code>, <code>_</code>, Groß-/Kleinschreibung, leere Filter</div></div>
  <div class="card flex gap-3 items-start"><span class="icon-circle gold"><span class="i-carbon-text-font" /></span><div class="small"><b>Eingaben</b><br>151 Zeichen, nur Leerzeichen, Umlaute, Emojis</div></div>
  <div class="card flex gap-3 items-start"><span class="icon-circle gold"><span class="i-carbon-link" /></span><div class="small"><b>URL</b><br>Filter in der URL manipulieren, unbekannte ID</div></div>
</div>

<div class="takeaway mt-4"><span class="i-carbon-arrow-right" /> Jeder echte Befund wird zu einem <strong>reproduzierbaren Test</strong> (siehe 3.5).</div>

<!--
Exploratives Testen ist eine Stärke des Agenten: Er probiert Dinge aus, an die ein Skript nicht denkt.
Wichtig: Befunde prüfen (halluzinierte Bugs) und in feste Tests überführen.
-->

---
step: 4
topic: 3.3 UI
---

# Playwright-Skript vs. Agent im Browser

| | <span class="i-logos-playwright" /> Playwright-Test (von KI geschrieben) | <span class="i-carbon-bot" /> Agent bedient den Browser |
|---|---|---|
| **Determinismus** | <span class="tag ok">gleiches Ergebnis bei jedem Lauf</span> | <span class="tag warn">kann jedes Mal anders vorgehen</span> |
| **Kosten pro Lauf** | <span class="tag ok">praktisch null</span> | <span class="tag bad">Tokens bei jedem Lauf</span> |
| **Laufzeit** | <span class="tag ok">Sekunden</span> | <span class="tag warn">Minuten</span> |
| **UI ändert sich** | <span class="tag warn">Test anpassen</span> | <span class="tag ok">passt sich an</span> |
| **Unerwartetes finden** | <span class="tag bad">nur was geprüft wird</span> | <span class="tag ok">exploriert selbst</span> |
| **CI-tauglich** | <span class="tag ok">ja</span> | <span class="tag warn">eingeschränkt</span> |
| **Nachvollziehbar** | <span class="tag ok">Code + Trace</span> | <span class="tag warn">Log + Screenshots</span> |

<div class="takeaway mt-4"><span class="i-carbon-arrow-right" /> Beides kombinieren: <strong>Agent erkundet</strong>, <strong>KI schreibt daraus Playwright-Tests</strong>, CI führt sie aus.</div>

---
step: 4
topic: 3.3 UI
---

# Bewertung: UI-Tests mit KI

<ProsCons>
<template #pros>

- Schreibt Playwright-Tests schnell, nutzt **`data-testid`**
- Agent im Browser braucht **keinen Testcode**
- **Exploratives Testen**: probiert Ungewöhnliches aus
- Kann Screenshots und Traces **interpretieren**
- Komponententests als **günstige Ebene** schnell erzeugt

</template>
<template #cons>

- Neigt zu **Sleeps** und fragilen Selektoren ohne Leitplanke
- **Testdaten** und Abhängigkeiten zwischen Tests → flaky
- Agent: **nicht deterministisch**, teuer, langsam
- Agent meldet ggf. **halluzinierte Befunde**
- E2E-Tests sind teuer: zu viele davon kippen die Pyramide

</template>
</ProsCons>

---
step: 5
topic: 3.3 UI
---

# Skill: `e2e-playwright`

<div class="grid grid-cols-[1.6fr_1fr] gap-5">

```markdown
---
name: e2e-playwright
description: UI-Tests für Prüfprot, als Playwright-Test (Java)
  oder explorativ per Browser-MCP
---
# UI testen
1. Selektoren nur über data-testid (alle interaktiven Elemente)
2. Keine festen Sleeps, Web-first Assertions nutzen
3. Jeder Test legt eigene Testdaten an (frische Klausur)
4. Nur wichtige Wege als E2E, Rest als Vitest-Komponententest
5. Stack: ./gradlew stackUp, dann ./gradlew e2eTest
   (BASE_URL, Standard http://localhost)
6. Explorativ: nach jedem Schritt Screenshot, Befunde mit
   Schritten + erwartet/tatsächlich festhalten
7. Befund bestätigt → als Playwright-Test reproduzieren
```

<SkillCollection current="e2e-playwright" />
</div>
