import { httpClient } from '../../../shared/api/httpClient'
import { extractData } from '../../../shared/api/extractData'
import type { TemplateDetail, TemplatesScrollData } from '../types'

type ListParams = {
  nextCursor?: string | null
  sort?: 'latest' | 'rating' | 'popular'
  order?: 'asc' | 'desc'
  categoryId?: number | null
  limit?: number
}

export const templatesApi = {
  
  // GET /api/templates?cursor=&sort=&order=&categoryId=&limit=
  async list(params?: ListParams): Promise<TemplatesScrollData> {
    const res = await httpClient.get('/api/templates', {
      params: {
        cursor: params?.nextCursor ?? undefined,
        sort: params?.sort ?? undefined,
        order: params?.order ?? undefined,
        categoryId: params?.categoryId ?? undefined,
        limit: params?.limit ?? undefined,
      },
    })
    return extractData<TemplatesScrollData>(res.data)
  },

  // GET /api/templates/{templateId}
  async detail(templateId: number): Promise<TemplateDetail> {
    const res = await httpClient.get(`/api/templates/${templateId}`)
    return extractData<TemplateDetail>(res.data)
  },
}
