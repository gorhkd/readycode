import { httpClient } from '../../../shared/api/httpClient'
import { extractData } from '../../../shared/api/extractData'

export type SignupRequest = {
  phoneNumber: string
  nickname: string
  purpose: string
  userRole: string
}

export type TokenResponse = {
  accessToken: string
  refreshToken?: string
  isRegistered?: boolean
}

export const usersApi = {
  async signup(tempAccessToken: string, body: SignupRequest): Promise<TokenResponse> {
    const res = await httpClient.post('/api/users/signup', body, {
      headers: {
        Authorization: `Bearer ${tempAccessToken}`, // ✅ 임시 토큰을 명시적으로 사용
      },
    })
    return extractData<TokenResponse>(res.data)
  },
}
