import type { LocationQuery, LocationQueryRaw } from 'vue-router'
import { emptyFilter, FILTER_KEYS, type ExamFilter } from './api'
import { isDegree } from './degrees'

/** Reads the search criteria from the URL (?university=…&subject=…). Unknown values are ignored. */
export function filterFromQuery(query: LocationQuery): ExamFilter {
  const filter = emptyFilter()
  for (const key of FILTER_KEYS) {
    const raw = query[key]
    const value = (Array.isArray(raw) ? raw[0] : raw) ?? ''
    if (key === 'degree') {
      filter.degree = isDegree(value) ? value : ''
    } else {
      filter[key] = value
    }
  }
  return filter
}

/** Only non-empty criteria end up in the URL. */
export function filterToQuery(filter: ExamFilter): LocationQueryRaw {
  const query: LocationQueryRaw = {}
  for (const key of FILTER_KEYS) {
    const value = filter[key].trim()
    if (value) {
      query[key] = value
    }
  }
  return query
}
