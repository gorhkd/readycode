import { useQuery } from '@tanstack/react-query'
import type {GetTemplatesQuery} from '@/api/templateApi.ts';
import {  getTemplates } from '@/api/templateApi.ts'

export default function useGetTemplates(params: GetTemplatesQuery) {
  return useQuery({
    queryKey: ['templates', params],
    queryFn: () => getTemplates(params),
  })
}
