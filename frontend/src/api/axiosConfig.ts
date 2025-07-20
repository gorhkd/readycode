import axios, { type AxiosResponse } from 'axios'

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
