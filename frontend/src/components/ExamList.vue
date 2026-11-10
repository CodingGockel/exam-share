<script setup lang="ts">
import type { Exam } from '../api'
import ExamItem from './ExamItem.vue'

defineProps<{
  exams: Exam[]
  loading: boolean
  error: string | null
}>()

const emit = defineEmits<{ voted: [] }>()
</script>

<template>
  <section class="exam-list" aria-label="Klausuren" :aria-busy="loading">
    <p v-if="error" class="message error" role="alert" data-testid="exam-list-error">{{ error }}</p>
    <p v-else-if="!loading && exams.length === 0" class="empty" data-testid="exam-list-empty">
      Keine Klausuren gefunden.
    </p>
    <ul v-else data-testid="exam-list">
      <ExamItem v-for="exam in exams" :key="exam.id" :exam="exam" @voted="emit('voted')" />
    </ul>
  </section>
</template>
