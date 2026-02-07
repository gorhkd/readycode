import { useEffect } from 'react'
import { useNavigate, useSearchParams, useParams } from 'react-router-dom'

export function AuthCallbackPage() {
  const navigate = useNavigate()
  const { provider } = useParams()
  const [searchParams] = useSearchParams()

  useEffect(() => {
    const accessToken =
      searchParams.get('accessToken') ||
      searchParams.get('token') ||
      searchParams.get('access_token')

    const error =
      searchParams.get('error') ||
      searchParams.get('message')

    if (error) return

    if (accessToken) {
      localStorage.setItem('access_token', accessToken)
      navigate('/templates', { replace: true })
      return
    }
  }, [navigate, searchParams])

  const error =
    searchParams.get('error') ||
    searchParams.get('message')

  return (
    <div>
      <h1>로그인 처리중...</h1>
      <div style={{ fontSize: 12, opacity: 0.8 }}>
        provider: {provider ?? '(unknown)'}
      </div>

      {error && (
        <div style={{ marginTop: 12 }}>
          실패: {error}
        </div>
      )}

      {!error && (
        <div style={{ marginTop: 12 }}>
          토큰을 받지 못했습니다. (백엔드가 쿠키 방식이거나 파라미터 키가 다를 수 있음)
        </div>
      )}
    </div>
  )
}
