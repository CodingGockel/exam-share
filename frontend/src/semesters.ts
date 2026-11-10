/** Oldest semester offered in the dropdowns. */
const FIRST_YEAR = 2000

function twoDigits(year: number): string {
  return String(year % 100).padStart(2, '0')
}

/**
 * All semesters from the current one back to SS00, newest first, e.g.
 * ["WS26/27", "SS26", "WS25/26", ..., "WS00/01", "SS00"].
 *
 * Summer semester: April to September. Winter semester: October to March
 * (January to March still belong to the winter semester that started the year before).
 */
export function buildSemesterOptions(now: Date = new Date()): string[] {
  const month = now.getMonth() + 1
  let year = now.getFullYear()
  let winter: boolean
  if (month >= 10) {
    winter = true
  } else if (month >= 4) {
    winter = false
  } else {
    winter = true
    year -= 1
  }

  const options: string[] = []
  while (year >= FIRST_YEAR) {
    if (winter) {
      options.push(`WS${twoDigits(year)}/${twoDigits(year + 1)}`)
      winter = false
    } else {
      options.push(`SS${twoDigits(year)}`)
      winter = true
      year -= 1
    }
  }
  return options
}
