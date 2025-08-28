import type { operations } from "@/api/dto.ts"
import api from "@/api/axiosConfig.ts"

/********** 타입 정리 ******************************/
// 템플릿 구매 (무료 담기)
export type PurchaseTemplatePath = operations['purchaseTemplate']['parameters']['path']['templateId']
type PurchaseTemplateResponse = operations['purchaseTemplate']['responses'][200]['content']['*/*']
// 마이페이지 구매 내역 조회
type GetMyPurchasesResponse = operations['getMyPurchases']['responses'][200]['content']['*/*']

/********** API 호출 함수 ******************************/
// 템플릿 구매 (무료 담기)
export async function purchaseTemplate(id: PurchaseTemplatePath) {
  const { data } = await api.post<PurchaseTemplateResponse>(`/purchases/${id}`)
  return data
}

// 마이페이지 구매 내역 조회
export async function getPurchaseHistory() {
  const { data } = await api.get<GetMyPurchasesResponse>('/purchases')
  return data
}