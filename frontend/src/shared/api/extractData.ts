type ApiEnvelope<T> = {
  status: number
  message: string
  data: T
}

export function extractData<T>(payload: unknown): T {
  // 백엔드 표준: { status, message, data }
  const p = payload as ApiEnvelope<T>
  return p.data
}