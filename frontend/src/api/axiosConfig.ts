import axios, { type AxiosError, type AxiosResponse } from 'axios'

interface APIResponse<T = never> {
  status: number
  message: string
  code?: string
  data?: T
}

const axiosInstance = axios.create({
  // baseURL: import.meta.env.VITE_API_URL,
  baseURL: '/api',
  timeout: 1000 * 10,
  headers: {
    'Content-Type': 'application/json',
  },
})

axiosInstance.interceptors.request.use((config) => {
  const token = '임시 토큰'
  if (token) {
    config.headers.Authorization = token
  }
  return config
})

axiosInstance.interceptors.response.use(
  (response: AxiosResponse) => {
    return response
  },

  async (error: AxiosError<APIResponse>) => {
    const isUnauthorized = error.status === 401

    if (error.response?.data) {
      if (isUnauthorized) {
        alert(error.response.data.message)
      }
      console.error('에러 내용: ', error.response?.data)
      return Promise.reject(error.response?.data)
    } else {
      console.error('에러 내용: 알 수 없는 API 에러가 발생했습니다.')
      return Promise.reject(error)
    }
  },
)

const api = {
  get: async <T = never>(url: string, config?: object): Promise<AxiosResponse<APIResponse<T>>> =>
    axiosInstance.get<APIResponse<T>>(url, config),
  post: async <T = never>(
    url: string,
    data?: object,
    config?: object,
  ): Promise<AxiosResponse<APIResponse<T>>> =>
    axiosInstance.post<APIResponse<T>>(url, data, config),
  put: async <T = never>(
    url: string,
    data?: object,
    config?: object,
  ): Promise<AxiosResponse<APIResponse<T>>> => axiosInstance.put<APIResponse<T>>(url, data, config),
  patch: async <T = never>(
    url: string,
    data?: object,
    config?: object,
  ): Promise<AxiosResponse<APIResponse<T>>> =>
    axiosInstance.patch<APIResponse<T>>(url, data, config),
  delete: async <T = never>(url: string, config?: object): Promise<AxiosResponse<APIResponse<T>>> =>
    axiosInstance.delete<APIResponse<T>>(url, config),
}

export default api
