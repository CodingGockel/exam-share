import type { Degree } from './api'

export const DEGREES: readonly { value: Degree; label: string }[] = [
  { value: 'BACHELOR', label: 'Bachelor' },
  { value: 'MASTER', label: 'Master' },
  { value: 'STAATSEXAMEN', label: 'Staatsexamen' },
]

export function degreeLabel(degree: Degree | null | undefined): string {
  return DEGREES.find((entry) => entry.value === degree)?.label ?? ''
}

export function isDegree(value: unknown): value is Degree {
  return DEGREES.some((entry) => entry.value === value)
}
