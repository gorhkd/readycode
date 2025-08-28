import api from "@/api/axiosConfig.ts"
import type { operations } from "@/api/dto.ts"

/********** 타입 정리 ******************************/
// 회원가입
export type SignupRequest = operations['signup']['requestBody']['content']['application/json']
type SignupResponse = operations['signup']['responses'][200]['content']['*/*']
// 회원 탈퇴 취소
type RestoreProfileResponse = operations['restoreProfile']['responses'][200]['content']['*/*']
// 회원 정보 조회
type GetProfileWithSocialInfoResponse = operations['getProfileWithSocialInfo']['responses'][200]['content']['*/*']
// 회원 탈퇴
type DeleteProfileResponse = operations['deleteProfile']['responses'][200]['content']['*/*']
// 회원 프로필 정보 수정
export type UpdateProfileRequest = operations['updateProfile']['requestBody']['content']['application/json']
type UpdateProfileResponse = operations['updateProfile']['responses'][200]['content']['*/*']

/********** API 호출 함수 ******************************/
// 회원가입
export async function signup(requestBody: SignupRequest) {
  const { data } = await api.post<SignupResponse>('/users/signup', requestBody)
  return data
}

// 회원 탈퇴 취소
export async function restoreMyProfile() {
  const { data } = await api.post<RestoreProfileResponse>('/users/me/restore')
  return data
}

// 회원 정보 조회
export async function getMyProfile() {
  const { data } = await api.get<GetProfileWithSocialInfoResponse>('/users/me')
  return data
}

// 회원 탈퇴
export async function deleteMyProfile() {
  const { data } = await api.delete<DeleteProfileResponse>('/users/me')
  return data
}

// 회원 프로필 정보 수정
export async function updateMyProfile(requestBody: UpdateProfileRequest) {
  const { data } = await api.patch<UpdateProfileResponse>('/users/me', requestBody)
  return data
}