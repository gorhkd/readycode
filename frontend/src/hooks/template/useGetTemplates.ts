import { useQuery } from '@tanstack/react-query'
import { getTemplates, type GetTemplatesQuery } from '@/api/templateApi.ts'

export default function useGetTemplates(params: GetTemplatesQuery) {
  return useQuery({
    queryKey: ['templates', params],
    queryFn: () => getTemplates(params),
  })
}
