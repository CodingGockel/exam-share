<script setup lang="ts">
import { onBeforeUnmount, reactive, ref, watch } from 'vue'
import { useRoute, useRouter, type LocationQueryRaw } from 'vue-router'
import { fetchExams, type Exam } from '../api'
import { filterFromQuery, filterToQuery } from '../filterQuery'
import FilterBar from '../components/FilterBar.vue'
import ExamList from '../components/ExamList.vue'

// The URL is the source of truth for the search, so results can be bookmarked and the back button works.

const FILTER_DEBOUNCE_MS = 300

const route = useRoute()
const router = useRouter()

const filter = reactive(filterFromQuery(route.query))
const exams = ref<Exam[]>([])
const loading = ref(false)
const loadError = ref<string | null>(null)

// Ignore responses of outdated requests (e.g. while typing in a filter)
let latestRequest = 0

async function loadExams() {
  const requestId = ++latestRequest
  loading.value = true
  loadError.value = null
  try {
    const result = await fetchExams(filterFromQuery(route.query))
    if (requestId === latestRequest) {
      exams.value = result
    }
  } catch (error) {
    if (requestId === latestRequest) {
      loadError.value = error instanceof Error ? error.message : 'Die Liste konnte nicht geladen werden.'
    }
  } finally {
    if (requestId === latestRequest) {
      loading.value = false
    }
  }
}

function sameQuery(a: LocationQueryRaw, b: LocationQueryRaw): boolean {
  return JSON.stringify(a) === JSON.stringify(b)
}

// URL changed (navigation, back button) -> update the form and reload
watch(
  () => route.query,
  (query) => {
    if (route.name !== 'search') {
      return // leaving the page
    }
    const fromUrl = filterFromQuery(query)
    if (!sameQuery(filterToQuery(fromUrl), filterToQuery(filter))) {
      Object.assign(filter, fromUrl)
    }
    void loadExams()
  },
  { immediate: true },
)

// Form changed -> write it to the URL (debounced), which triggers the reload above
let debounceTimer: ReturnType<typeof setTimeout> | undefined
watch(filter, () => {
  clearTimeout(debounceTimer)
  debounceTimer = setTimeout(() => {
    const query = filterToQuery(filter)
    if (!sameQuery(query, filterToQuery(filterFromQuery(route.query)))) {
      void router.replace({ name: 'search', query })
    }
  }, FILTER_DEBOUNCE_MS)
})
onBeforeUnmount(() => clearTimeout(debounceTimer))
</script>

<template>
  <h1 class="page-title">Klausuren suchen</h1>
  <FilterBar v-model="filter" />
  <p v-if="!loading && !loadError" class="result-count" data-testid="result-count">
    {{ exams.length === 1 ? '1 Klausur gefunden' : `${exams.length} Klausuren gefunden` }}
  </p>
  <ExamList :exams="exams" :loading="loading" :error="loadError" @voted="loadExams" />
</template>
