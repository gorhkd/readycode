import type { operations } from "@/api/dto.ts"
import api from "@/api/axiosConfig.ts"

/********** 타입 정리 ******************************/
// 인증 토큰 재발급
export type ReissueRequest = operations['reissue']['requestBody']['content']['application/json']
type ReissueResponse = operations['reissue']['responses'][200]['content']['*/*']
// 로그아웃
export type LogoutRequest = operations['logout']['requestBody']['content']['application/json']
type LogoutResponse = operations['logout']['responses'][200]['content']['*/*']
// SNS별 로그인
export type LoginRequest = operations['login']['requestBody']['content']['application/json']
type LoginResponse = operations['login']['responses'][200]['content']['*/*']

/********** API 호출 함수 ******************************/
// 인증 토큰 재발급
export async function reissueToken(requestBody: ReissueRequest) {
  const { data } = await api.post<ReissueResponse>('auth/reissue', requestBody)
  return data
}

// 로그아웃
export async function logout(requestBody: LogoutRequest) {
  const { data } = await api.post<LogoutResponse>('auth/logout', requestBody)
  return data
}

// SNS별 로그인
export async function login(requestBody: LoginRequest) {
  const { data } = await api.post<LoginResponse>('auth/login', requestBody)
  return data
}