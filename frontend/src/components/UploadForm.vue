<script setup lang="ts">
import { reactive, ref, useTemplateRef } from 'vue'
import { RouterLink } from 'vue-router'
import { uploadExam, type Exam } from '../api'
import { filterToQuery } from '../filterQuery'
import { refreshSuggestions, useSuggestions } from '../suggestions'
import SemesterSelect from './SemesterSelect.vue'
import DegreeSelect from './DegreeSelect.vue'
import type { Degree } from '../api'

const suggestions = useSuggestions()

const submitting = ref(false)
const errorMessage = ref<string | null>(null)
const uploaded = ref<Exam | null>(null)
const form = reactive({
  university: '',
  faculty: '',
  studyProgram: '',
  degree: '' as Degree | '',
  subject: '',
  lecturer: '',
  semester: '',
})
const file = ref<File | null>(null)
const fileInput = useTemplateRef<HTMLInputElement>('fileInput')

function onFileChange(event: Event) {
  const input = event.target as HTMLInputElement
  file.value = input.files?.[0] ?? null
}

/** Keeps university, faculty, program and degree: several exams of one program are often uploaded in a row. */
function resetExamFields() {
  form.subject = ''
  form.lecturer = ''
  form.semester = ''
  file.value = null
  if (fileInput.value) {
    fileInput.value.value = ''
  }
}

function searchQueryFor(exam: Exam) {
  return filterToQuery({
    university: exam.university,
    faculty: '',
    studyProgram: '',
    degree: '',
    subject: exam.subject,
    lecturer: '',
    semester: '',
  })
}

async function submit() {
  errorMessage.value = null
  uploaded.value = null
  if (!file.value) {
    errorMessage.value = 'Bitte wähle eine PDF-Datei aus.'
    return
  }
  submitting.value = true
  try {
    uploaded.value = await uploadExam({ ...form, file: file.value })
    resetExamFields()
    void refreshSuggestions()
  } catch (error) {
    errorMessage.value = error instanceof Error ? error.message : 'Der Upload ist fehlgeschlagen.'
  } finally {
    submitting.value = false
  }
}
</script>

<template>
  <form class="card upload-form" data-testid="upload-form" @submit.prevent="submit">
    <fieldset>
      <legend>Hochschule</legend>
      <div class="form-grid">
        <label>
          Uni *
          <input
            v-model="form.university"
            type="text"
            required
            maxlength="150"
            list="upload-universities"
            placeholder="z. B. Universität Leipzig"
            data-testid="upload-university"
          />
        </label>
        <label>
          Fakultät *
          <input
            v-model="form.faculty"
            type="text"
            required
            maxlength="100"
            list="upload-faculties"
            data-testid="upload-faculty"
          />
        </label>
        <label>
          Studiengang *
          <input
            v-model="form.studyProgram"
            type="text"
            required
            maxlength="100"
            list="upload-programs"
            placeholder="z. B. Informatik"
            data-testid="upload-program"
          />
        </label>
        <label>
          Abschluss *
          <DegreeSelect v-model="form.degree" empty-label="Bitte wählen" required data-testid="upload-degree" />
        </label>
      </div>
    </fieldset>

    <fieldset>
      <legend>Klausur</legend>
      <div class="form-grid">
        <label>
          Modul *
          <input
            v-model="form.subject"
            type="text"
            required
            maxlength="100"
            placeholder="z. B. Analysis I"
            data-testid="upload-subject"
          />
        </label>
        <label>
          Dozent:in *
          <input v-model="form.lecturer" type="text" required maxlength="100" data-testid="upload-lecturer" />
        </label>
        <label>
          Semester *
          <SemesterSelect v-model="form.semester" empty-label="Bitte wählen" required data-testid="upload-semester" />
        </label>
        <label>
          Datei (PDF, max. 10 MB) *
          <input
            ref="fileInput"
            type="file"
            accept="application/pdf,.pdf"
            required
            data-testid="upload-file"
            @change="onFileChange"
          />
        </label>
      </div>
    </fieldset>

    <div class="form-actions">
      <button type="submit" class="primary" :disabled="submitting" data-testid="upload-submit">
        {{ submitting ? 'Wird hochgeladen …' : 'Hochladen' }}
      </button>
      <span class="hint">Alle Felder sind Pflicht.</span>
    </div>

    <p v-if="errorMessage" class="message error" role="alert" data-testid="upload-error">{{ errorMessage }}</p>
    <p v-if="uploaded" class="message success" role="status" data-testid="upload-success">
      „{{ uploaded.subject }}“ wurde hochgeladen.
      <RouterLink :to="{ name: 'search', query: searchQueryFor(uploaded) }" data-testid="upload-success-link">
        In der Suche ansehen
      </RouterLink>
    </p>

    <datalist id="upload-universities">
      <option v-for="value in suggestions.universities" :key="value" :value="value" />
    </datalist>
    <datalist id="upload-faculties">
      <option v-for="value in suggestions.faculties" :key="value" :value="value" />
    </datalist>
    <datalist id="upload-programs">
      <option v-for="value in suggestions.studyPrograms" :key="value" :value="value" />
    </datalist>
  </form>
</template>
