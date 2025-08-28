import api from "@/api/axiosConfig.ts"
import type { operations } from "@/api/dto.ts"

/********** 타입 정리 ******************************/
// 조회
type GetAllCategoriesResponse = operations['getAllCategories']['responses'][200]['content']['*/*']
// 생성
export type CreateCategoryRequest = operations['createCategory']['requestBody']['content']['application/json']
type CreateCategoryResponse = operations['createCategory']['responses'][200]['content']['*/*']
// 삭제
export type DeleteCategoryPath = operations['deleteCategory']['parameters']['path']['categoriesId']
type DeleteCategoryResponse = operations['deleteCategory']['responses'][200]['content']['*/*']
// 수정
export type UpdateCategoryPath = operations['updateCategory']['parameters']['path']['categoriesId']
type UpdateCategoryResponse = operations['updateCategory']['responses'][200]['content']['*/*']

/********** API 호출 함수 ******************************/
// 조회
export async function getCategories() {
  const { data } = await api.get<GetAllCategoriesResponse>('/categories')
  return data
}

// 생성
export async function createCategory(requestBody: CreateCategoryRequest) {
  const { data } = await api.post<CreateCategoryResponse>('/categories', requestBody)
  return data
}

// 삭제
export async function deleteCategory(id: DeleteCategoryPath) {
  const { data } = await api.delete<DeleteCategoryResponse>(`/categories/${id}`)
  return data
}

// 수정
export async function updateCategory(id: UpdateCategoryPath) {
  const { data } = await api.patch<UpdateCategoryResponse>(`/categories/${id}`)
  return data
}