<script setup lang="ts">
import { buildSemesterOptions } from '../semesters'

defineProps<{
  /** Text of the empty option, e.g. "Alle Semester" or "Bitte wählen". */
  emptyLabel: string
  required?: boolean
}>()

const model = defineModel<string>({ required: true })
const options = buildSemesterOptions()
</script>

<template>
  <select v-model="model" :required="required">
    <option value="" :disabled="required">{{ emptyLabel }}</option>
    <option v-for="semester in options" :key="semester" :value="semester">{{ semester }}</option>
  </select>
</template>
