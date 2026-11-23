import { defineMermaidSetup } from '@slidev/types'

// Mermaid-Diagramme in den Farben der Uni-Vorlage.
export default defineMermaidSetup(() => ({
  theme: 'base',
  // Akteure nur oben zeigen, spart Höhe auf den Folien
  sequence: { mirrorActors: false },
  themeVariables: {
    fontFamily: 'Roboto Condensed Variable, Roboto Condensed, sans-serif',
    fontSize: '15px',
    primaryColor: '#002f5d',
    primaryTextColor: '#ffffff',
    primaryBorderColor: '#002f5d',
    secondaryColor: '#ae9a63',
    tertiaryColor: '#f2f2f2',
    lineColor: '#7682a5',
    textColor: '#002f5d',
    actorBkg: '#002f5d',
    actorTextColor: '#ffffff',
    actorBorder: '#002f5d',
    actorLineColor: '#a8afc8',
    signalColor: '#002f5d',
    signalTextColor: '#002f5d',
    labelBoxBkgColor: '#ae9a63',
    labelTextColor: '#ffffff',
    noteBkgColor: '#f2f2f2',
    noteTextColor: '#002f5d',
    noteBorderColor: '#ae9a63',
    activationBkgColor: '#e3e7f0',
    sequenceNumberColor: '#ffffff',
  },
}))
