import { ref } from 'vue'
import { fetchSuggestions, type Suggestions } from './api'

// Shared across all pages; loaded on first use and refreshed after an upload.
const suggestions = ref<Suggestions>({ universities: [], faculties: [], studyPrograms: [] })
let loaded = false

export async function refreshSuggestions(): Promise<void> {
  try {
    suggestions.value = await fetchSuggestions()
    loaded = true
  } catch {
    // suggestions are optional; inputs still work without them
  }
}

export function useSuggestions() {
  if (!loaded) {
    void refreshSuggestions()
  }
  return suggestions
}
