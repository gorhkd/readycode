import type { operations } from '@/api/dto.ts'
import api from '@/api/axiosConfig.ts'

/********** 타입 정리 ******************************/
// 조회
export type GetTemplatesQuery = operations['getTemplates']['parameters']['query']
type GetTemplatesResponse = operations['getTemplates']['responses'][200]['content']['*/*']
// 생성
export type CreateTemplatesRequest = operations['createTemplate']['requestBody']['content']['application/json']
type CreateTemplateResponse = operations['createTemplate']['responses'][200]['content']['*/*']
// 개별 조회
export type GetDetailsTemplatePath = operations['detailsTemplate']['parameters']['path']['templatesId']
type GetDetailsTemplateResponse = operations['detailsTemplate']['responses'][200]['content']['*/*']
// 개별 삭제
export type DeleteTemplatePath = operations['deleteTemplate']['parameters']['path']['templatesId']
type DeleteTemplateResponse = operations['deleteTemplate']['responses'][200]['content']['*/*']
// 개별 수정
export type ModifyTemplatePath = operations['modifyTemplate']['parameters']['path']['templatesId']
type ModifyTemplateRequest = operations['modifyTemplate']['requestBody']['content']['application/json']
type ModifyTemplateResponse = operations['modifyTemplate']['responses'][200]['content']['*/*']

/********** API 호출 함수 ******************************/
// 조회
export async function getTemplates(params: GetTemplatesQuery = {}) {
  const { data } = await api.get<GetTemplatesResponse>('/templates', { params })
  return data
}

// 생성
export async function createTemplate(requestBody: CreateTemplatesRequest = {
  title: '테스트 템플릿 1',
  description: '테스트 설명글',
  price: 1000,
  categoryId: 1,
  image: 'imageString'
}) {
  const { data } = await api.post<CreateTemplateResponse>('/templates', requestBody)
  return data
}

// 개별 조회
export async function getTemplateById(id: GetDetailsTemplatePath) {
  const { data } = await api.get<GetDetailsTemplateResponse>(`/templates/${id}`)
  return data
}

// 개별 삭제
export async function deleteTemplateById(id: DeleteTemplatePath) {
  const { data } = await api.delete<DeleteTemplateResponse>(`/templates/${id}`)
  return data
}

// 개별 수정
export async function modifyTemplateById(id: ModifyTemplatePath, requestBody: ModifyTemplateRequest) {
  const { data } = await api.patch<ModifyTemplateResponse>(`/templates/${id}`, requestBody)
  return data
}