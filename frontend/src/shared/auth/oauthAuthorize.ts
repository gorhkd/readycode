
type Provider = 'google' | 'kakao' | 'naver'

const REDIRECT_BASE = 'https://readycode.shop'
const REDIRECT_PATH = '/oauth'

const CLIENT_ID: Record<Provider, string> = {
  google: '1080388753904-6ji6fmof7lliqda5t0lnpvlc8giv6kgh.apps.googleusercontent.com',
  kakao: '5a5d934f0670bb288849e9c02ccf0aaa',
  naver: '_o_LmBP4F36mEG_YBQIU',
}

function redirectUri(provider: Provider) {
  return `${REDIRECT_BASE}${REDIRECT_PATH}/${provider}`
}

export function buildAuthorizeUrl(provider: Provider): string {
  const redirect = redirectUri(provider)

  if (provider === 'google') {
    const params = new URLSearchParams({
      client_id: CLIENT_ID.google,
      redirect_uri: redirect,
      response_type: 'code',
      scope: 'openid email profile',
    })
    return `https://accounts.google.com/o/oauth2/v2/auth?${params.toString()}`
  }

  if (provider === 'kakao') {
    const params = new URLSearchParams({
      client_id: CLIENT_ID.kakao,
      redirect_uri: redirect,
      response_type: 'code',
    })
    return `https://kauth.kakao.com/oauth/authorize?${params.toString()}`
  }

  const params = new URLSearchParams({
    response_type: 'code',
    client_id: CLIENT_ID.naver,
    redirect_uri: redirect,
    state: crypto.randomUUID(), // ✅ 네이버는 state 사실상 필수(보안/요구사항)
  })
  return `https://nid.naver.com/oauth2.0/authorize?${params.toString()}`
}
