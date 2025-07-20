import axios, { type AxiosResponse } from 'axios'

const axiosInstance = axios.create({
  // baseURL: import.meta.env.VITE_API_URL,
  baseURL: '/api',
  timeout: 1000 * 10,
  headers: {
    'Content-Type': 'application/json',
  },
})

interface APIResponse<T = never> {
  metaData: any
  data: T
}

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
