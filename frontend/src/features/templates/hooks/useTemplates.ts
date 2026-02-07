import { useInfiniteQuery, useQuery } from '@tanstack/react-query'
import { templatesApi } from '../api/templatesApi'

export function useTemplateDetailQuery(id: number) {
  return useQuery({
    queryKey: ['templates', 'detail', id],
    queryFn: () => templatesApi.detail(id),
    enabled: Number.isFinite(id) && id > 0,
  })
}

export function useTemplatesInfiniteQuery(limit = 10) {
  return useInfiniteQuery({
    queryKey: ['templates', 'infinite', { limit }],
    initialPageParam: null as string | null,

    queryFn: ({ pageParam }) => templatesApi.list({ nextCursor: pageParam, limit }),

    getNextPageParam: (lastPage) => lastPage.nextCursor ?? undefined,
  })
}