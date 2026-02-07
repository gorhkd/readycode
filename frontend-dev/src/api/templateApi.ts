import api from '@/api/axiosConfig.ts'
import type { operations } from '@/api/dto.ts'

export type GetTemplatesQuery = operations['getTemplates']['parameters']['query']
type GetTemplatesResponse = operations['getTemplates']['responses'][200]['content']['*/*']

export async function getTemplates(params: GetTemplatesQuery = {}) {
  try {
    const { data } = await api.get<GetTemplatesResponse>('/templates', { params })
    return data
  } catch (error) {
    throw error
  }
}
