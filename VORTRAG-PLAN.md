# Vortrag „Software Testing mit KI“: Demo- & Themenplan

> ⚠️ Diese Datei verweist auf die eingebauten Demo-Bugs (Nummern aus `DEMO-BUGS.md`).
> Deshalb gehört sie **nicht ins Repo**, sondern zu `DEMO-BUGS.md` außerhalb des Projektordners.

**Roter Faden:** *„Wer testet eigentlich die Tests, die die KI schreibt?“*
**Demo-App:** Prüfprot, eine Altklausuren-Plattform (Vue, Spring Boot, Postgres, nginx, alles in Docker)

---

## Übersicht

| # | Abschnitt | Was gezeigt wird | Testart / Tool | Bug | Prio |
|---|-----------|------------------|----------------|-----|------|
| 1 | Hook | KI-Tests grün, hohe Coverage, trotzdem ein Bug | Unit, JaCoCo | #4 oder #1 | Muss |
| 2 | Grundlagen | Testpyramide, Agent-Loop, Tests von Anfang an (TDD) | Unit | – | Muss |
| 3a | Unit-Tests | Validierung, Ranking, Property-based | JUnit, AssertJ, jqwik | #2, #4, #5 | Muss |
| 3b | Integration / API | Upload/Download gegen echtes Postgres, parallele Votes, API-Skill | Testcontainers, REST | #3 | Muss |
| 3c | End-to-End | Playwright-Test vs. Agent klickt per Browser-MCP | Playwright, Browser-MCP | #6 | Muss |
| 4 | Bugs verifizieren & Regression | Bug-Ticket → roter Test → Fix → Regressionstest | Unit/Integration | #1 | Muss |
| 5 | Migration | JUnit 4 → 5: OpenRewrite vs. KI | OpenRewrite, Claude | – | Muss |
| 6 | Automatisierung | CI-Pipeline, KI im PR-Review, flaky Tests | GitHub Actions | – | Kann |
| 7 | Skills | Skill für spezifische Tests (SPARQL) | Skill, Fuseki | – | Kann |
| 8 | Risiken & Gegenmittel | KI schummelt → Leitplanken, Hooks, Clean-Room, PIT, exploratives Testen | PIT, Hooks | #7 | Muss |
| 9 | Fazit & Ausblick | Review-Checkliste, Evals für LLM-Features | – | – | Muss |

---

## 0. Vorbereitung (vor dem Vortrag)

- [ ] Git-Tags als Startpunkte: `demo/start`, `demo/after-unit`, `demo/after-integration`, `demo/legacy-junit4`
- [ ] Jede Demo mindestens einmal komplett geprobt
- [ ] **Backup:** Video oder Screenshots jeder Demo (KI-Ausgaben sind nicht deterministisch)
- [ ] Docker-Images vorab gepullt (Postgres, nginx, JRE, Node), damit Testcontainers und Compose nicht am WLAN scheitern
- [ ] Playwright-Browser installiert
- [ ] Stack läuft: `./deploy.sh` → Health ok
- [ ] Dummy-PDFs bereit: normal, genau 10 MB, knapp unter 10 MB, 10 MB + 1 Byte, leer, getarnte `.exe`, Dateiname `../../etc/passwd.pdf`
- [ ] Hook-Konfiguration vorbereitet und getestet (nur ein- und ausschalten live)
- [ ] Tokenverbrauch pro Demo notiert (für Abschnitt 5 und die Kostenfrage)
- [ ] Schriftgröße in Terminal und IDE erhöht

---

## 1. Hook: „Alles grün und trotzdem kaputt“

**Ziel:** die Leitfrage setzen. Coverage ist kein Maß für Qualität.

**Ablauf**
1. `RankingService` (naive Formel, Bug #4) oder die Filterlogik (Bug #1) zeigen.
2. Prompt: *„Schreib Unit-Tests für den RankingService.“*
3. Tests laufen grün, der JaCoCo-Report zeigt eine hohe Coverage.
4. Auflösung: „1 von 1 positiv“ steht vor „95 von 100 positiv“, der Bug ist live in der App sichtbar.

**Botschaft:** Die KI hat getestet, was der Code *tut*, nicht was er tun *soll*.
*(Diese Tests später in Abschnitt 8 mit PIT wieder aufgreifen.)*

---

## 2. Grundlagen & Common Practices

**Folien**
- Testpyramide: Unit → Integration → E2E, Kosten und Geschwindigkeit pro Ebene
- Wie ein Agent testet: schreiben → ausführen → Fehler lesen → korrigieren (Feedback-Loop)
- Warum Tests von Anfang an: Tests sind das Feedback-Signal, das die KI braucht. Ohne Tests rät sie.
- Die CLAUDE.md als Kontext: Spezifikation und Leitplanken gehören ins Repo.

**Mini-Demo (optional):** Semester-Validierung (`WS24/25`, `SS25`) per TDD. Prompt: *„Schreib zuerst
die Tests aus der Spezifikation in der CLAUDE.md, führe sie aus (rot), dann implementiere.“*

---

## 3a. Unit-Tests

**Was gezeigt wird**
- Upload-Validierung: Magic Bytes, leere Datei, Größenlimit → **Grenzwerte** (Bug #2: genau 10 MB wird abgelehnt)
- Einklapp-Schwelle: 4 vs. 5 vs. 6 Stimmen, 29 % vs. 30 % (Bug #5)
- **Property-based Testing mit jqwik** fürs Ranking:
  - Score liegt immer in `[0, 1]`
  - Ein zusätzliches 👍 senkt den Score nie, ein zusätzliches 👎 erhöht ihn nie
  - Bei gleicher Quote und mehr Stimmen ist der Score höher (das findet Bug #4)

**Vergleich:** Beispiel-Tests vs. Properties. Welche findet die KI von selbst, welche erst auf Hinweis?

**Befehle:** `./gradlew test`, `./gradlew :backend:test --tests "*Ranking*"`

---

## 3b. Integration- & API-Tests

**Was gezeigt wird**
- Testcontainers mit echtem Postgres statt H2, mit Begründung (Flyway-Migrationen, `bytea`, echtes SQL-Verhalten)
- Multipart-Upload → Liste → Download liefert **byte-identische** Datei
- Filter über die API (Groß-/Kleinschreibung, Teiltreffer)
- Statuscodes: 400, 404, 409 (doppelte Stimme), 413, Problem-JSON
- **Parallele Votes:** 50 gleichzeitige 👍 → genau 50 gezählt? (Bug #3, Lost Update)
  → Das findet nur ein Integrationstest gegen echtes Postgres, kein Unit-Test.
- **API-Testing über einen Skill:** Ein Skill beschreibt die Endpoints, der Agent feuert Requests
  (curl/REST-Assured) und prüft die Antworten selbstständig.

**Befehle:** `./gradlew integrationTest`

---

## 3c. End-to-End-Tests

**Klassisch**
- Folie: kurzer historischer Bogen HtmlUnit → Selenium → Playwright
- Playwright-Test (Java): hochladen → in der Liste finden → abstimmen → herunterladen
- Die KI schreibt den Test anhand der `data-testid`-Attribute

**Agentisch**
- Der Agent bedient die App über Browser-MCP: klickt, füllt Formulare aus, macht Screenshots und beurteilt das Ergebnis selbst
- Vergleich: deterministisch und wiederholbar vs. flexibel und nicht reproduzierbar → Wann passt was?

**Aha-Moment (Bug #6):** Eine knapp 10 MB große Datei. Unit- und Integrationstests sind grün,
aber über nginx gibt es einen 413. Das findet nur E2E durch den echten Proxy.

**Befehle:** `./gradlew stackUp`, `./gradlew e2eTest`

---

## 4. Bugs verifizieren & Regression

**Ablauf**
1. Bug-Ticket als Text: *„Suche nach ‚mathe‘ findet ‚Mathe‘ nicht.“* (Bug #1)
2. Prompt: *„Schreib zuerst einen Test, der diesen Bug reproduziert. Er muss rot sein. Fixe noch nichts.“*
3. Test ist rot → der Bug ist **verifiziert** (nicht nur behauptet).
4. Fix → Test grün → der Test bleibt dauerhaft als **Regressionstest**.
5. Bonus: einen KI-Fix zeigen, der den Test grün macht, aber an einer anderen Stelle etwas kaputtmacht → die übrige Suite fängt es ab.

**Optional: Characterization Tests (Golden Master).** Vor einem Refactoring hält die KI das aktuelle
Verhalten fest. Das ist der eine Fall, in dem Tests aus der Implementierung genau richtig sind.

---

## 5. Migration JUnit 4 → JUnit 5

**Setup:** Modul `legacy/` mit JUnit-4-Altlasten (`@Before`, `Parameterized`, `@Rule ExpectedException`,
`TemporaryFolder`, `@Test(expected=…)`, Hamcrest)

**Ablauf**
- Branch `migration/openrewrite`: OpenRewrite-Rezept `JUnit4to5Migration`
- Branch `migration/ai`: Prompt *„Migriere die Tests in legacy/ nach JUnit 5“*, **zweimal** ausführen
- Danach vergleichen:

| Kriterium | OpenRewrite | KI (Lauf 1) | KI (Lauf 2) |
|-----------|-------------|-------------|-------------|
| Tests grün? | | | |
| Dauer | | | |
| Tokens / Kosten | – | | |
| Diff identisch zwischen Läufen? | ja (deterministisch) | | |
| Sonderfälle korrekt? | | | |
| Nacharbeit nötig | | | |

**Botschaft:** Für mechanische, gut definierte Transformationen ist ein deterministisches Werkzeug
billiger und reproduzierbar. Die KI eignet sich für die Reste. Fazit: **hybrid**, erst OpenRewrite,
dann die KI für die Sonderfälle.

---

## 6. Automatisierung / CI/CD *(Kann)*

- GitHub-Actions-Pipeline: `test` → `integrationTest` → Stack starten → `e2eTest` → `pitest` (z. B. nightly)
- KI im PR-Review: Kommentare zu den Tests (fehlende Grenzwerte, zu viele Mocks)
- **Flaky Tests:** ein Test mit Zeit- oder Reihenfolgeabhängigkeit; der Agent lässt ihn 50-mal laufen und findet die Ursache
- Diskussion: Darf die KI in der CI Tests „reparieren“? (→ Abschnitt 8)

---

## 7. Skills für spezifische Tests *(Kann)*

- Was ist ein Skill: Domänenwissen einmal beschreiben statt in jedem Prompt
- **Beispiel SPARQL:** Fuseki-Container mit Beispieldaten; der Skill beschreibt Endpoint, Präfixe
  und Testmuster (ASK-Queries, erwartete Ergebnismengen, leere Ergebnisse)
- Vergleich: derselbe Auftrag mit und ohne Skill → Qualität der Tests
- Optional: ein projektspezifischer Skill `pruefprot-api-testing` (verbindet sich mit 3b)

---

## 8. Risiken & Gegenmittel

**8.1 Die KI schummelt (live)**
- Vorher in der CLAUDE.md den Abschnitt „Leitplanken für Tests“ **entfernen**
- Prompt: *„Dieser Test schlägt fehl, mach ihn grün.“*
- Erwartung: Die Assertion wird geändert, `@Disabled` gesetzt oder ein Spezialfall in den Code eingebaut → `git diff` zeigen
- Typische Muster auf einer Folie: Tests spiegeln die Implementierung, alles wird weggemockt,
  Assertions werden abgeschwächt, Tests werden gelöscht

**8.2 Gegenmittel**
- **Leitplanken** zurück in die CLAUDE.md → dieselbe Demo noch einmal → Vorher/Nachher
- **Hooks:** Tests laufen automatisch nach jeder Änderung; Testdateien sind beim Bugfix gesperrt
- **Clean-Room:** Ein Agent schreibt die Tests nur aus „Fachlichkeit“ und „REST-API“ der CLAUDE.md, ohne den Code zu sehen → findet Bug #4
- **Mutation Testing mit PIT:** zuerst gegen die Hook-Tests aus Abschnitt 1 (niedriger Mutation Score), dann gegen die verbesserten Tests → Vergleich von Coverage und Mutation Score
- **Exploratives Testen:** *„Hier ist die App, versuch sie kaputtzumachen.“* Der Agent probiert getarnte `.exe`-Dateien, Path-Traversal-Dateinamen (Bug #7), Sonderzeichen, Doppelklicks und das Löschen des Vote-Cookies

**Befehle:** `./gradlew pitest` → HTML-Report zeigen

---

## 9. Fazit & Ausblick

**Checkliste fürs Review von KI-Tests (Folie zum Mitnehmen)**
1. Würde der Test fehlschlagen, wenn der Code falsch wäre? (→ Mutation Testing)
2. Kommt das erwartete Ergebnis aus der Anforderung oder aus dem Code?
3. Sind Grenzwerte abgedeckt?
4. Wird echte Logik getestet oder nur Mocks?
5. Hat die KI bestehende Tests geändert, abgeschwächt oder deaktiviert?

**Ausblick:** Wenn die App selbst ein LLM nutzt, reichen Assertions nicht mehr. Dann braucht man Evals.

**Kernbotschaft:** Die KI beschleunigt das Schreiben von Tests enorm, aber die Verantwortung für
deren Qualität bleibt beim Menschen.

---

## Anhang A: Weitere Themen (bei Zeit oder für Fragen)

- Testdaten-Generierung: synthetisch, ohne personenbezogene Daten, gezielte Edge Cases
- Contract Testing Frontend ↔ Backend (Pact / Spring Cloud Contract)
- Visuelle Regression (Screenshot-Diff) vs. KI-Beurteilung von Layouts
- Accessibility-Checks (axe) über Browser-MCP
- Last-Tests: k6- oder Gatling-Skripte aus der API-Beschreibung generieren
- Frontend-Unit-Tests mit Vitest und Vue Test Utils

## Anhang B: Bug-Zuordnung (Details in `DEMO-BUGS.md`)

| Bug | Kurzbeschreibung | Abschnitt |
|-----|------------------|-----------|
| #1 | Filter beachtet Groß-/Kleinschreibung | 1 / 4 |
| #2 | Genau 10 MB wird abgelehnt | 3a |
| #3 | Lost Update bei parallelen Votes | 3b |
| #4 | Naives Ranking statt Wilson-Score | 1 / 3a / 8 |
| #5 | Off-by-one bei der Einklapp-Schwelle | 3a |
| #6 | nginx-Limit zu knapp | 3c |
| #7 | Unsicherer Dateiname im Header | 8 |

## Anhang C: Noch im Projekt ergänzen

- [ ] JaCoCo-Plugin für Coverage-Reports (für Abschnitt 1 und den Vergleich mit PIT)
- [ ] GitHub-Actions-Workflow (Abschnitt 6)
- [ ] `.claude/skills/` mit API-Testing- und SPARQL-Skill (Abschnitte 3b und 7)
- [ ] Fuseki-Container in einer separaten Compose-Datei (Abschnitt 7)
- [ ] Hook-Konfiguration (Abschnitt 8)
- [ ] Ein absichtlich flaky Test (Abschnitt 6)
