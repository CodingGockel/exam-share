<script setup lang="ts">
import { onMounted, reactive, ref, useTemplateRef } from 'vue'
import { useRouter } from 'vue-router'
import { emptyFilter } from '../api'
import { filterToQuery } from '../filterQuery'
import { useSuggestions } from '../suggestions'
import SemesterSelect from './SemesterSelect.vue'
import DegreeSelect from './DegreeSelect.vue'

// Search form on the landing page: university and module are required, everything else narrows the results.

const router = useRouter()
const suggestions = useSuggestions()
const filter = reactive(emptyFilter())
const showMore = ref(false)
const universityInput = useTemplateRef<HTMLInputElement>('universityInput')

onMounted(() => universityInput.value?.focus())

function submit() {
  void router.push({ name: 'search', query: filterToQuery(filter) })
}
</script>

<template>
  <form class="card search-form" data-testid="search-form" @submit.prevent="submit">
    <div class="form-grid">
      <label>
        Uni *
        <input
          ref="universityInput"
          v-model="filter.university"
          type="text"
          required
          list="search-universities"
          placeholder="z. B. Universität Leipzig"
          data-testid="search-university"
        />
      </label>
      <label>
        Modul *
        <input
          v-model="filter.subject"
          type="text"
          required
          placeholder="z. B. Analysis I"
          data-testid="search-subject"
        />
      </label>
    </div>

    <button
      type="button"
      class="link-button"
      :aria-expanded="showMore"
      aria-controls="search-more"
      data-testid="search-more-toggle"
      @click="showMore = !showMore"
    >
      {{ showMore ? 'Weniger Filter' : 'Weitere Filter' }}
    </button>

    <div v-show="showMore" id="search-more" class="form-grid">
      <label>
        Fakultät
        <input v-model="filter.faculty" type="text" list="search-faculties" data-testid="search-faculty" />
      </label>
      <label>
        Studiengang
        <input v-model="filter.studyProgram" type="text" list="search-programs" data-testid="search-program" />
      </label>
      <label>
        Abschluss
        <DegreeSelect v-model="filter.degree" empty-label="Alle Abschlüsse" data-testid="search-degree" />
      </label>
      <label>
        Dozent:in
        <input v-model="filter.lecturer" type="text" data-testid="search-lecturer" />
      </label>
      <label>
        Semester
        <SemesterSelect v-model="filter.semester" empty-label="Alle Semester" data-testid="search-semester" />
      </label>
    </div>

    <div class="form-actions">
      <button type="submit" class="primary" data-testid="search-submit">Suchen</button>
    </div>

    <datalist id="search-universities">
      <option v-for="value in suggestions.universities" :key="value" :value="value" />
    </datalist>
    <datalist id="search-faculties">
      <option v-for="value in suggestions.faculties" :key="value" :value="value" />
    </datalist>
    <datalist id="search-programs">
      <option v-for="value in suggestions.studyPrograms" :key="value" :value="value" />
    </datalist>
  </form>
</template>
