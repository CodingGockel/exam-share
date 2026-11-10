<script setup lang="ts">
import { computed, ref } from 'vue'
import { downloadUrl, voteExam, type Exam, type VoteDirection } from '../api'
import { degreeLabel } from '../degrees'

const props = defineProps<{ exam: Exam }>()
const emit = defineEmits<{ voted: [] }>()

const expanded = ref(false)
const voting = ref(false)
const voteError = ref<string | null>(null)

const showDetails = computed(() => !props.exam.collapsed || expanded.value)
const uploadDate = computed(() => new Date(props.exam.createdAt).toLocaleDateString('de-DE'))
const studyProgram = computed(() => {
  const program = props.exam.studyProgram ?? ''
  const degree = degreeLabel(props.exam.degree)
  if (program && degree) {
    return `${program} (${degree})`
  }
  return program || degree
})
const institution = computed(() =>
  [props.exam.university, props.exam.faculty, studyProgram.value].filter((part) => part).join(' · '),
)

async function vote(direction: VoteDirection) {
  voteError.value = null
  voting.value = true
  try {
    await voteExam(props.exam.id, direction)
    emit('voted')
  } catch (error) {
    voteError.value = error instanceof Error ? error.message : 'Die Stimme konnte nicht gezählt werden.'
  } finally {
    voting.value = false
  }
}
</script>

<template>
  <li
    class="card exam"
    :class="{ collapsed: exam.collapsed && !expanded }"
    data-testid="exam-item"
    :data-exam-id="exam.id"
    :data-collapsed="exam.collapsed"
  >
    <div class="exam-header">
      <h2 class="exam-subject" data-testid="exam-subject">{{ exam.subject }}</h2>
      <span class="exam-semester" data-testid="exam-semester">{{ exam.semester }}</span>
    </div>

    <template v-if="showDetails">
      <p class="exam-institution" data-testid="exam-institution">{{ institution }}</p>
      <p class="exam-meta">
        <span data-testid="exam-lecturer">{{ exam.lecturer }}</span>
        ·
        <span>hochgeladen am <time :datetime="exam.createdAt" data-testid="exam-date">{{ uploadDate }}</time></span>
      </p>

      <div class="exam-actions">
        <button
          type="button"
          class="vote"
          :disabled="voting"
          aria-label="Positiv bewerten"
          data-testid="vote-up"
          @click="vote('UP')"
        >
          👍 <span data-testid="upvote-count">{{ exam.upvotes }}</span>
        </button>
        <button
          type="button"
          class="vote"
          :disabled="voting"
          aria-label="Negativ bewerten"
          data-testid="vote-down"
          @click="vote('DOWN')"
        >
          👎 <span data-testid="downvote-count">{{ exam.downvotes }}</span>
        </button>
        <a class="button download" :href="downloadUrl(exam.id)" download data-testid="exam-download">Herunterladen</a>
      </div>

      <p v-if="voteError" class="message error" role="alert" data-testid="vote-error">{{ voteError }}</p>
    </template>

    <div v-else class="collapsed-hint">
      <span>Eingeklappt wegen überwiegend negativer Bewertungen.</span>
      <button type="button" data-testid="exam-expand" @click="expanded = true">Trotzdem anzeigen</button>
    </div>
  </li>
</template>
