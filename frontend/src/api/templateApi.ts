import type {operations} from '@/api/dto.ts'
import api from '@/api/axiosConfig.ts'

export type GetTemplatesQuery = operations['getTemplates']['parameters']['query']
type GetTemplatesResponse = operations['getTemplates']['responses'][200]['content']['*/*']

export async function getTemplates(params: GetTemplatesQuery = {}) {
    const { data } = await api.get<GetTemplatesResponse>('/templates', { params })
    return data
}
