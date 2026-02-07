export type ApiError = {
  status: number
  message: string
  code?: string
  detail?: unknown
}

export function isApiError(e: unknown): e is ApiError {
  return typeof e === 'object' && e !== null && 'status' in e && 'message' in e
}

export function getErrorMessage(e: unknown): string {
  if (isApiError(e)) return e.message
  if (e instanceof Error) return e.message
  if (typeof e === 'string') return e
  return '알 수 없는 오류가 발생했습니다.'
}