// Client for the backend REST API. Always relative to /api (served by the same origin via the proxy).

export type Degree = 'BACHELOR' | 'MASTER' | 'STAATSEXAMEN'

export interface Exam {
  id: string
  university: string
  faculty: string | null
  studyProgram: string | null
  degree: Degree | null
  subject: string
  lecturer: string
  semester: string
  originalFilename: string
  sizeBytes: number
  upvotes: number
  downvotes: number
  score: number
  collapsed: boolean
  createdAt: string
}

/** All search criteria; empty strings mean "no filter". Keys match the API query parameters. */
export interface ExamFilter {
  university: string
  faculty: string
  studyProgram: string
  degree: Degree | ''
  subject: string
  lecturer: string
  semester: string
}

export const FILTER_KEYS = [
  'university',
  'faculty',
  'studyProgram',
  'degree',
  'subject',
  'lecturer',
  'semester',
] as const satisfies readonly (keyof ExamFilter)[]

export function emptyFilter(): ExamFilter {
  return { university: '', faculty: '', studyProgram: '', degree: '', subject: '', lecturer: '', semester: '' }
}

export interface Suggestions {
  universities: string[]
  faculties: string[]
  studyPrograms: string[]
}

export type VoteDirection = 'UP' | 'DOWN'

export interface VoteResult {
  examId: string
  upvotes: number
  downvotes: number
  score: number
  collapsed: boolean
}

export interface UploadData {
  university: string
  faculty: string
  studyProgram: string
  degree: Degree | ''
  subject: string
  lecturer: string
  semester: string
  file: File
}

export class ApiError extends Error {
  readonly status: number

  constructor(status: number, message: string) {
    super(message)
    this.name = 'ApiError'
    this.status = status
  }
}

const BASE = '/api'

const FALLBACK_MESSAGES: Record<number, string> = {
  400: 'Die Eingaben sind ungültig.',
  404: 'Die Klausur wurde nicht gefunden.',
  409: 'Du hast für diese Klausur bereits abgestimmt.',
  413: 'Die Datei ist zu groß.',
}

async function toApiError(response: Response): Promise<ApiError> {
  let detail: string | undefined
  const contentType = response.headers.get('Content-Type') ?? ''
  if (contentType.includes('json')) {
    try {
      const body = (await response.json()) as { detail?: unknown }
      if (typeof body.detail === 'string' && body.detail.length > 0) {
        detail = body.detail
      }
    } catch {
      // ignore unreadable error bodies
    }
  }
  const message =
    detail ?? FALLBACK_MESSAGES[response.status] ?? `Unerwarteter Fehler (Status ${response.status}).`
  return new ApiError(response.status, message)
}

async function request<T>(path: string, init?: RequestInit): Promise<T> {
  let response: Response
  try {
    response = await fetch(BASE + path, init)
  } catch {
    throw new ApiError(0, 'Der Server ist nicht erreichbar.')
  }
  if (!response.ok) {
    throw await toApiError(response)
  }
  return (await response.json()) as T
}

export function fetchExams(filter: ExamFilter): Promise<Exam[]> {
  const params = new URLSearchParams()
  for (const key of FILTER_KEYS) {
    const value = filter[key].trim()
    if (value) {
      params.set(key, value)
    }
  }
  const query = params.toString()
  return request<Exam[]>(query ? `/exams?${query}` : '/exams')
}

export function fetchSuggestions(): Promise<Suggestions> {
  return request<Suggestions>('/exams/suggestions')
}

export function uploadExam(data: UploadData): Promise<Exam> {
  const body = new FormData()
  body.append('university', data.university)
  body.append('faculty', data.faculty)
  body.append('studyProgram', data.studyProgram)
  body.append('degree', data.degree)
  body.append('subject', data.subject)
  body.append('lecturer', data.lecturer)
  body.append('semester', data.semester)
  body.append('file', data.file)
  return request<Exam>('/exams', { method: 'POST', body })
}

export function voteExam(id: string, direction: VoteDirection): Promise<VoteResult> {
  return request<VoteResult>(`/exams/${encodeURIComponent(id)}/votes`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ direction }),
  })
}

export function downloadUrl(id: string): string {
  return `${BASE}/exams/${encodeURIComponent(id)}/download`
}
