import { useEffect, useState } from 'react'
import { useNavigate, useParams, useSearchParams } from 'react-router-dom'
import { httpClient } from '../shared/api/httpClient'
import { extractData } from '../shared/api/extractData'
import { authSession } from '../shared/auth/authSession'

type TokenResponse = {
  accessToken: string
  isRegistered?: boolean
}

const LOGIN_REDIRECT_KEY = 'login_redirect_to'

export function OAuthCallbackPage() {
  const navigate = useNavigate()
  const { provider } = useParams()
  const [searchParams] = useSearchParams()
  const [statusText, setStatusText] = useState('로그인 처리중...')

  useEffect(() => {
    const code = searchParams.get('code')
    const error = searchParams.get('error') || searchParams.get('error_description')

    if (!provider) {
      setStatusText('provider가 없습니다. (경로 확인 필요)')
      return
    }
    if (error) {
      setStatusText(`SNS 로그인 실패: ${error}`)
      return
    }
    if (!code) {
      setStatusText('인가 코드(code)가 없습니다. (redirect-uri/라우팅 확인 필요)')
      return
    }

    const redirectTo = sessionStorage.getItem(LOGIN_REDIRECT_KEY) ?? '/templates'

    ;(async () => {
      try {
        setStatusText('백엔드 로그인 요청중...')

        const res = await httpClient.post('/api/auth/login', {
          provider,
          authCode: code,
        })

        const token = extractData<TokenResponse>(res.data)

        authSession.setAccessToken(token.accessToken)

        sessionStorage.removeItem(LOGIN_REDIRECT_KEY)

        if (token.isRegistered === false) {
          // 회원가입 화면은 다음 단계에서 붙일 것
          setStatusText('추가 회원가입이 필요합니다. (다음 단계에서 /signup 붙일 예정)')
          navigate('/signup', { replace: true })
          return
        }

        setStatusText('로그인 성공! 이동중...')
        navigate(redirectTo, { replace: true })
      } catch (e: any) {
        setStatusText(`로그인 처리 실패: ${e?.message ?? '알 수 없음'}`)
      }
    })()
  }, [navigate, provider, searchParams])

  return (
    <div>
      <h1>OAuth Callback</h1>
      <div style={{ marginTop: 8 }}>{statusText}</div>
      <div style={{ marginTop: 8, fontSize: 12, opacity: 0.7 }}>
        provider: {provider ?? '(없음)'}
      </div>
    </div>
  )
}
