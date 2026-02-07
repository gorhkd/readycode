import axios from 'axios'
import type { AxiosError } from 'axios'
import type { ApiError } from './apiError'
import { authSession } from '../auth/authSession'

const LOGIN_REDIRECT_KEY = 'login_redirect_to'

export const httpClient = axios.create({
  baseURL: '',
  timeout: 15_000,
})

httpClient.interceptors.request.use((config) => {
  const token = authSession.getAccessToken()
  if (token) {
    config.headers = config.headers ?? {}
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

httpClient.interceptors.response.use(
  (res) => res,
  (err: AxiosError) => {
    const status = err.response?.status ?? 0
    const data: unknown = err.response?.data
    const detail = data

    const apiError: ApiError = { status, message: '요청 실패', detail }

    if (data && typeof data === 'object') {
      const d = data as { message?: unknown; code?: unknown }
      if (d.message) apiError.message = String(d.message)
      if (d.code) apiError.code = String(d.code)
    }

    const url = err.config?.url ?? ''
    const isLogout = url.includes('/api/auth/logout')

    const hadToken = !!authSession.getAccessToken()

    if (status === 401) {
      authSession.clear()

      if (!isLogout && hadToken) {
        if (window.location.pathname !== '/login') {
          sessionStorage.setItem(LOGIN_REDIRECT_KEY, window.location.pathname)
          window.location.href = '/login'
        }
      }
    }

    return Promise.reject(apiError)
  }
)
