<script setup lang="ts">
import type { Degree } from '../api'
import { DEGREES } from '../degrees'

defineProps<{
  /** Text of the empty option, e.g. "Alle Abschlüsse" or "Bitte wählen". */
  emptyLabel: string
  required?: boolean
}>()

const model = defineModel<Degree | ''>({ required: true })
</script>

<template>
  <select v-model="model" :required="required">
    <option value="" :disabled="required">{{ emptyLabel }}</option>
    <option v-for="degree in DEGREES" :key="degree.value" :value="degree.value">{{ degree.label }}</option>
  </select>
</template>
