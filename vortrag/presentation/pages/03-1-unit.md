---
layout: section
part: '3.1'
---

# Unit-Tests

Die kleinste Einheit: isoliert, schnell, viele davon

::visual::
<TestPyramid :width="330" active="unit" />

---
step: 1
topic: 3.1 Unit
---

# Was ist ein Unit-Test?

<ScopeStack scope="unit" />

<div class="grid grid-cols-3 gap-4 mt-5">
  <div class="card flex gap-3 items-start">
    <span class="icon-circle"><span class="i-carbon-flash" /></span>
    <div><h3>Schnell</h3><span class="small">Millisekunden pro Test, kein Docker, kein Netzwerk. Läuft bei jeder Änderung.</span></div>
  </div>
  <div class="card flex gap-3 items-start">
    <span class="icon-circle"><span class="i-carbon-center-circle" /></span>
    <div><h3>Präzise</h3><span class="small">Schlägt ein Test fehl, zeigt er auf <b>eine</b> Klasse und eine Regel.</span></div>
  </div>
  <div class="card flex gap-3 items-start">
    <span class="icon-circle"><span class="i-carbon-repeat" /></span>
    <div><h3>Deterministisch</h3><span class="small">Gleiche Eingabe, gleiches Ergebnis. Keine Uhrzeit, kein Zufall, keine DB.</span></div>
  </div>
</div>

<div class="takeaway mt-4">Unit-Tests sind das <strong>Fundament der Pyramide</strong>: Sie prüfen Fachlogik, nicht das Zusammenspiel.</div>

<!--
Unit = eine Klasse oder Funktion. Abhängigkeiten (Repository, DB) werden ersetzt.
In Prüfprot ist die Fachlogik bewusst in kleine Klassen ausgelagert, die man ohne Spring testen kann.
-->

---
step: 1
topic: 3.1 Unit
---

# Kandidaten in Prüfprot

<div class="grid grid-cols-2 gap-4">
  <div class="card-white flex gap-3">
    <span class="icon-circle gold"><span class="i-carbon-calendar" /></span>
    <div>
      <h3><code>SemesterValidator</code></h3>
      <ul class="small">
        <li>Format <code>WS24/25</code> oder <code>SS25</code></li>
        <li>Bei WS müssen die Jahre aufeinander folgen</li>
      </ul>
    </div>
  </div>
  <div class="card-white flex gap-3">
    <span class="icon-circle gold"><span class="i-carbon-chart-line" /></span>
    <div>
      <h3><code>RankingService</code></h3>
      <ul class="small">
        <li>Score = Wilson-Untergrenze, ohne Stimmen 0</li>
        <li>Eingeklappt ab 5 Stimmen und unter 30 % 👍</li>
      </ul>
    </div>
  </div>
  <div class="card-white flex gap-3">
    <span class="icon-circle gold"><span class="i-carbon-document-pdf" /></span>
    <div>
      <h3><code>PdfUploadValidator</code></h3>
      <ul class="small">
        <li>Content-Type <b>und</b> Magic Bytes <code>%PDF-</code></li>
        <li>Leer → abgelehnt, genau 10 MB → erlaubt</li>
      </ul>
    </div>
  </div>
  <div class="card-white flex gap-3">
    <span class="icon-circle gold"><span class="i-carbon-text-clear-format" /></span>
    <div>
      <h3><code>FilenameSanitizer</code></h3>
      <ul class="small">
        <li>Dateiname nur Metadatum, nie Pfad</li>
        <li>Sicherer <code>Content-Disposition</code>-Header</li>
      </ul>
    </div>
  </div>
</div>

<p class="muted small mt-3"><span class="i-carbon-information" /> Alles reine Funktionen mit klaren Regeln aus der Spezifikation: ideal für Unit-Tests und für die KI.</p>

---
step: 2
topic: 3.1 Unit
---

# Klassisch: Aufbau und Werkzeuge

<div class="grid grid-cols-[1.35fr_1fr] gap-6">
<div>

```java
@Test
void oneOfOneRanksBelowNinetyFiveOfHundred() {
    // Arrange
    var ranking = new RankingService();

    // Act
    double few  = ranking.score(1, 0);    // 1 von 1 positiv
    double many = ranking.score(95, 5);   // 95 von 100 positiv

    // Assert
    assertThat(many).isGreaterThan(few);
}
```

<FlowSteps small class="mt-3" :steps="[
  { title: 'Arrange', text: 'Ausgangslage herstellen', color: 'blue' },
  { title: 'Act', text: 'genau eine Aktion', color: 'navy' },
  { title: 'Assert', text: 'Ergebnis prüfen', color: 'gold' },
]" />

</div>
<div class="tools">
  <div class="tool"><span class="i-logos-java ico" /><div><b>JUnit 5</b><br><span class="small muted">Testframework, <code>@ParameterizedTest</code></span></div></div>
  <div class="tool"><span class="i-carbon-checkmark-outline ico" /><div><b>AssertJ</b><br><span class="small muted">lesbare Assertions</span></div></div>
  <div class="tool"><span class="i-carbon-dicom-overlay ico" /><div><b>Mockito</b><br><span class="small muted">nur an Systemgrenzen</span></div></div>
  <div class="tool"><span class="i-carbon-chart-scatter ico" /><div><b>jqwik</b><br><span class="small muted">Property-based Testing</span></div></div>
  <div class="tool"><span class="i-logos-vitest ico" /><div><b>Vitest</b><br><span class="small muted">Frontend-Logik (TypeScript)</span></div></div>
  <div class="tool"><span class="i-logos-gradle ico" /><div><b><code>./gradlew test</code></b><br><span class="small muted">alle Module, ein Befehl</span></div></div>
</div>
</div>

<style>
.tools { display: flex; flex-direction: column; gap: 7px; }
.tool { display: flex; align-items: center; gap: 10px; font-size: 16px; line-height: 1.2; }
.tool .ico { font-size: 26px; width: 30px; flex: none; color: var(--uni-navy); }
</style>

<!--
Arrange-Act-Assert (bzw. Given-When-Then aus Teil 1) als feste Struktur.
Die erwarteten Werte kommen aus der Spezifikation: "1 von 1 darf nicht vor 95 von 100 landen."
-->

---
step: 2
topic: 3.1 Unit
---

# Klassisch: Äquivalenzklassen und Grenzwerte

<div class="mt-1">

<BoundaryLine title="Upload: Dateigröße (Standardlimit 10 MB)"
  :zones="[
    { label: 'leer', flex: 0.6, ok: false },
    { label: 'erlaubt: 1 Byte bis genau 10 MB', flex: 3, ok: true },
    { label: 'zu groß → 413', flex: 1.4, ok: false },
  ]"
  :ticks="[
    { at: 0.012, label: '0 B', ok: false },
    { at: 0.135, label: '1 B', ok: true },
    { at: 0.70, label: '10 MB', ok: true },
    { at: 0.775, label: '10 MB + 1 B', ok: false },
  ]" />

<BoundaryLine title="Einklappen: Anzahl Stimmen (alle 👎)"
  :zones="[
    { label: 'unter 5 Stimmen: sichtbar', flex: 1, ok: true },
    { label: 'ab 5 Stimmen: eingeklappt', flex: 1, ok: false },
  ]"
  :ticks="[
    { at: 0.44, label: '4 Stimmen', ok: true },
    { at: 0.56, label: '5 Stimmen', ok: false },
  ]" />

<BoundaryLine title="Einklappen: Anteil 👍 bei 10 Stimmen"
  :zones="[
    { label: 'unter 30 %: eingeklappt', flex: 1, ok: false },
    { label: 'ab 30 %: sichtbar', flex: 1.6, ok: true },
  ]"
  :ticks="[
    { at: 0.33, label: '2 von 10', ok: false },
    { at: 0.43, label: '3 von 10', ok: true },
  ]" />

</div>

<div class="takeaway">Fehler sitzen an den Kanten: Ein <code class="!bg-white/15 !text-white">&lt;</code> statt <code class="!bg-white/15 !text-white">&lt;=</code> fällt nur auf, wer <strong>genau auf und direkt neben</strong> der Grenze testet.</div>

<!--
Äquivalenzklassen: Werte, die sich gleich verhalten sollen. Pro Klasse reicht ein Vertreter,
plus die Grenzwerte direkt auf und neben der Grenze.
Später in der Demo: Findet die KI diese Grenzen von selbst oder erst auf Hinweis?
-->

---
step: 2
topic: 3.1 Unit
---

# Property-based Testing

<div class="grid grid-cols-[1fr_1.45fr] gap-5">
<div>
  <div class="card mb-3">
    <h3><span class="i-carbon-list" /> Beispielbasiert</h3>
    <span class="small">„Für <b>1 👍 / 0 👎</b> ist der Score ≈ 0,21.“<br>Prüft die Fälle, an die man gedacht hat.</span>
  </div>
  <div class="card-white" style="border-color: var(--uni-gold)">
    <h3><span class="i-carbon-chart-scatter" /> Eigenschaftsbasiert</h3>
    <span class="small">„Für <b>alle</b> Stimmen gilt: Score ∈ [0, 1]. Ein weiteres 👍 senkt ihn nie.“<br>jqwik erzeugt standardmäßig 1000 Eingaben pro Eigenschaft und <b>schrumpft</b> ein Gegenbeispiel auf den kleinsten Fall.</span>
  </div>
  <p class="small muted mt-2"><span class="i-carbon-information" /> Die Eigenschaften stehen wörtlich in der Spezifikation („Eigenschaften, die immer gelten müssen“).</p>
</div>
<div>

```java
@Property
void scoreStaysWithinZeroAndOne(
        @ForAll @IntRange(max = 1_000_000) int up,
        @ForAll @IntRange(max = 1_000_000) int down) {
    assertThat(ranking.score(up, down)).isBetween(0.0, 1.0);
}

@Property
void anotherUpvoteNeverLowersTheScore(
        @ForAll @IntRange(max = 1_000_000) int up,
        @ForAll @IntRange(max = 1_000_000) int down) {
    assertThat(ranking.score(up + 1, down))
            .isGreaterThanOrEqualTo(ranking.score(up, down));
}
```

</div>
</div>

<!--
Property-based Testing passt perfekt zum Ranking, weil die Spezifikation Eigenschaften formuliert
statt konkrete Werte. Die dritte Eigenschaft (ein weiteres 👎 erhöht den Score nie) ist analog.
-->

---
step: 2
topic: 3.1 Unit
---

# Das Ranking als Testobjekt: Warum Wilson?

<div class="grid grid-cols-[1.3fr_1fr] gap-6 items-center">
<WilsonChart />
<div>

- Naiver Anteil: **1 von 1** 👍 landet vor **95 von 100**
- Wilson-Untergrenze (95 %, z = 1,96) bestraft **wenige Stimmen**
- Ohne Stimmen: Score **0**

<div class="card mt-3 small">
<b><span class="i-carbon-warning-alt gold" /> Das Orakel-Problem</b><br>
Ein Test, der die Formel einfach nachrechnet, prüft nichts. Gute Orakel sind
<b>Ranking-Vergleiche</b> aus der Spezifikation, <b>bekannte Werte</b> und <b>Eigenschaften</b>.
</div>

</div>
</div>

<!--
Die Grafik ist live berechnet (components/WilsonChart.vue).
Die Ränge zeigen, wie sich die Reihenfolge gegenüber dem naiven Anteil verschiebt.
Überleitung zur KI: Woher nimmt die KI die erwarteten Werte? Aus dem Code wäre zirkulär.
-->

---
step: 3
topic: 3.1 Unit
---

# KI-Demo: Unit-Tests aus der Spezifikation

<div class="grid grid-cols-[1fr_1.25fr] gap-5 h-full">
<div class="flex flex-col gap-3">

<PromptBox>

Schreib Unit-Tests für `SemesterValidator` und `RankingService`.
Leite die erwarteten Werte aus den Anforderungen in der `CLAUDE.md` ab,
**nicht aus der Implementierung**. Führe die Tests danach aus.

</PromptBox>

<div class="small">

**Worauf achten im Video?**

- Woher kommen die erwarteten Werte?
- Findet die KI die **Grenzwerte** von selbst?
- Nutzt sie den **Feedback-Loop** (ausführen, Fehler lesen)?
- Was tut sie bei einem **roten Test**?

</div>
</div>
<DemoVideo title="Unit-Tests für Validierung und Ranking" duration="ca. 1–2 min" />
</div>

---
step: 3
topic: 3.1 Unit
---

# KI-Demo: Mit und ohne Hinweis auf Grenzwerte

<div class="grid grid-cols-[1.3fr_1fr] gap-6">
<div>

| Grenzfall aus der Spezifikation | ohne Hinweis | mit Hinweis / Skill |
|---|:---:|:---:|
| Upload genau 10 MB / 10 MB + 1 B | ☐ | ☐ |
| Leere Datei | ☐ | ☐ |
| 4 vs. 5 Stimmen (Einklappen) | ☐ | ☐ |
| 2/10 vs. 3/10 positiv (30 %) | ☐ | ☐ |
| `WS24/25` gültig, `WS24/26` ungültig | ☐ | ☐ |
| Ohne Stimmen: Score 0 | ☐ | ☐ |

<p class="tiny muted mt-1">Nach der Aufnahme ausfüllen: ☑ = Test vorhanden, ☐ = fehlt</p>

</div>
<div class="flex flex-col gap-3">
<PromptBox label="Variante A">

Schreib Unit-Tests für den `RankingService`.

</PromptBox>
<PromptBox label="Variante B">

… **Teste alle Grenzwerte explizit**: genau auf der Grenze und direkt daneben.

</PromptBox>
<DemoVideo title="Vergleich A vs. B" duration="ca. 1 min" class="!min-h-[90px] flex-1" />
</div>
</div>

<!--
Ein Satz im Prompt (oder im Skill) kann die Testqualität deutlich ändern.
Tabelle nach der Aufnahme ausfüllen, nicht vorher raten.
-->

---
step: 3
topic: 3.1 Unit
---

# Testdaten-Generierung mit KI

<div class="grid grid-cols-[1.25fr_1fr] gap-6">
<div>

| Eingabe | erwartet | Begründung |
|---|:---:|---|
| `WS24/25` | <span class="tag ok">gültig</span> | Jahre folgen aufeinander |
| `SS25` | <span class="tag ok">gültig</span> | Sommersemester |
| `WS24/26` | <span class="tag bad">ungültig</span> | Jahre nicht aufeinander |
| `SS2025` | <span class="tag bad">ungültig</span> | falsches Format |
| `WS99/00` | <span class="tag warn">offen</span> | Jahrhundertwechsel? |
| `ws24/25` | <span class="tag warn">offen</span> | Groß-/Kleinschreibung? |

</div>
<div class="flex flex-col gap-3">
  <div class="card flex gap-3"><span class="icon-circle"><span class="i-carbon-data-1" /></span><div class="small"><b>Realistische Daten</b><br>Unis, Module, Dozent:innen für Seed-Daten und Demos</div></div>
  <div class="card flex gap-3"><span class="icon-circle"><span class="i-carbon-text-font" /></span><div class="small"><b>Sonderfälle</b><br>Umlaute, Emojis, 150 Zeichen, <code>%</code> und <code>_</code> in der Suche</div></div>
  <div class="card flex gap-3"><span class="icon-circle"><span class="i-carbon-document-pdf" /></span><div class="small"><b>Dateien</b><br>leeres PDF, genau 10 MB, getarnte <code>.exe</code>, Dateiname <code>../../etc/passwd.pdf</code></div></div>
</div>
</div>

<div class="takeaway mt-4"><span class="i-carbon-help" /> Gelbe Fälle sind <strong>Fragen an die Spezifikation</strong>, keine Testfälle: Die KI soll nachfragen, nicht raten.</div>

<!--
Die KI erzeugt in Sekunden viele Testdaten. Spannend sind die Fälle, bei denen die Spezifikation
keine Antwort gibt: Daran sieht man, ob die KI rät oder nachfragt.
-->

---
step: 4
topic: 3.1 Unit
---

# Bewertung: Unit-Tests mit KI

<ProsCons>
<template #pros>

- Schreibt in Sekunden viele Tests inkl. **Parametrisierung**
- Leitet Fälle aus einer **klaren Spezifikation** gut ab
- Nutzt den **Feedback-Loop**: ausführen, Fehler lesen, korrigieren
- Gut bei **reinen Funktionen** wie Validierung und Ranking
- Erzeugt **Testdaten** und Sonderfälle schnell

</template>
<template #cons>

- **Spiegelt die Implementierung**: Ein Bug wird zum erwarteten Wert
- Ohne Hinweis: Risiko, nur den **Happy Path** zu testen
- Rät bei **unklarer Spezifikation**, statt nachzufragen
- Neigt zu **Mocks** und redundanten Tests
- **Hohe Coverage** sagt nichts über die Aussagekraft

</template>
<template #note>
<div class="takeaway"><span class="i-carbon-arrow-right" /> Ob die Tests etwas taugen, prüfen wir später mit <strong>Mutation Testing (PIT)</strong> in Teil 5.</div>
</template>
</ProsCons>

---
step: 5
topic: 3.1 Unit
---

# Skill: `unit-testing`

<div class="grid grid-cols-[1.6fr_1fr] gap-5">

```markdown
---
name: unit-testing
description: Unit-Tests für das Prüfprot-Backend (JUnit 5,
  AssertJ, jqwik) aus der Spezifikation ableiten
---
# Unit-Tests schreiben
1. Erwartete Werte aus der CLAUDE.md ableiten, nie aus dem Code
2. Pro Regel: Äquivalenzklassen + Grenzwerte (auf, knapp
   darunter, knapp darüber)
3. Eigenschaften aus der Spezifikation mit jqwik (@Property)
4. Nur Systemgrenzen mocken, keine eigene Fachlogik
5. Unklare Fälle: nachfragen statt raten
6. Roter Test = möglicher Bug → melden, Assertion nicht ändern
7. Ausführen: ./gradlew :backend:test
```

<SkillCollection current="unit-testing" />
</div>

<!--
Der Skill hält fest, was wir in diesem Kapitel gelernt haben. Die Sammlung rechts wächst mit jedem Kapitel.
-->
