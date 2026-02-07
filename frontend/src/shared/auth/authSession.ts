const ACCESS_TOKEN_KEY = 'access_token'
const REFRESH_TOKEN_KEY = 'refresh_token'

export const authSession = {
  getAccessToken(): string | null {
    return sessionStorage.getItem(ACCESS_TOKEN_KEY)
  },

  setAccessToken(token: string) {
    sessionStorage.setItem(ACCESS_TOKEN_KEY, token)
  },

  getRefreshToken(): string | null {
    return sessionStorage.getItem(REFRESH_TOKEN_KEY)
  },

  setRefreshToken(token: string) {
    sessionStorage.setItem(REFRESH_TOKEN_KEY, token)
  },

  clear() {
    sessionStorage.removeItem(ACCESS_TOKEN_KEY)
    sessionStorage.removeItem(REFRESH_TOKEN_KEY)
  },

  isLoggedIn(): boolean {
    return !!sessionStorage.getItem(ACCESS_TOKEN_KEY)
  },
}
