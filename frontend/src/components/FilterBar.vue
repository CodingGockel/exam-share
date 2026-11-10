<script setup lang="ts">
import type { ExamFilter } from '../api'
import { emptyFilter } from '../api'
import { useSuggestions } from '../suggestions'
import SemesterSelect from './SemesterSelect.vue'
import DegreeSelect from './DegreeSelect.vue'

// All filters on the results page; every change updates the list.

const filter = defineModel<ExamFilter>({ required: true })
const suggestions = useSuggestions()

function reset() {
  Object.assign(filter.value, emptyFilter())
}
</script>

<template>
  <section class="card filter-bar" aria-label="Filter">
    <div class="form-grid filter-grid">
      <label>
        Uni
        <input v-model="filter.university" type="search" list="filter-universities" data-testid="filter-university" />
      </label>
      <label>
        Modul
        <input v-model="filter.subject" type="search" data-testid="filter-subject" />
      </label>
      <label>
        Dozent:in
        <input v-model="filter.lecturer" type="search" data-testid="filter-lecturer" />
      </label>
      <label>
        Fakultät
        <input v-model="filter.faculty" type="search" list="filter-faculties" data-testid="filter-faculty" />
      </label>
      <label>
        Studiengang
        <input v-model="filter.studyProgram" type="search" list="filter-programs" data-testid="filter-program" />
      </label>
      <label>
        Abschluss
        <DegreeSelect v-model="filter.degree" empty-label="Alle Abschlüsse" data-testid="filter-degree" />
      </label>
      <label>
        Semester
        <SemesterSelect v-model="filter.semester" empty-label="Alle Semester" data-testid="filter-semester" />
      </label>
      <div class="filter-reset">
        <button type="button" data-testid="filter-reset" @click="reset">Filter zurücksetzen</button>
      </div>
    </div>

    <datalist id="filter-universities">
      <option v-for="value in suggestions.universities" :key="value" :value="value" />
    </datalist>
    <datalist id="filter-faculties">
      <option v-for="value in suggestions.faculties" :key="value" :value="value" />
    </datalist>
    <datalist id="filter-programs">
      <option v-for="value in suggestions.studyPrograms" :key="value" :value="value" />
    </datalist>
  </section>
</template>
