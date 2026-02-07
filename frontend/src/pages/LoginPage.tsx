import { useEffect } from 'react'
import { useLocation, useNavigate } from 'react-router-dom'
import { buildAuthorizeUrl } from '../shared/auth/oauthAuthorize'
import { authSession } from '../shared/auth/authSession'
import { Button } from '../shared/ui/Button'

const LOGIN_REDIRECT_KEY = 'login_redirect_to'

export function LoginPage() {
  const location = useLocation()
  const navigate = useNavigate()

  useEffect(() => {
    if (authSession.isLoggedIn()) {
      navigate('/templates', { replace: true })
    }
  }, [navigate])

  useEffect(() => {
    const from = (location.state as any)?.from
    if (typeof from === 'string' && from.startsWith('/')) {
      sessionStorage.setItem(LOGIN_REDIRECT_KEY, from)
    } else {
      sessionStorage.setItem(LOGIN_REDIRECT_KEY, '/templates')
    }
  }, [location.state])

  const login = (provider: 'google' | 'kakao' | 'naver') => {
    window.location.href = buildAuthorizeUrl(provider)
  }

  return (
    <div className="mx-auto mt-12 w-full max-w-lg">
      <div className="rounded-2xl border border-zinc-200 bg-white p-8 shadow-sm dark:border-zinc-800 dark:bg-zinc-950">
        <div className="mb-6">
          <h1 className="text-2xl font-semibold">로그인</h1>
        </div>

         <div className="space-y-4">
            <Button
              variant="plain"
              fullWidth
              onClick={() => login('google')}
              className="py-3 text-base border border-zinc-200 bg-white text-zinc-900 hover:bg-zinc-50 dark:border-zinc-700 dark:bg-zinc-900 dark:text-white dark:hover:bg-zinc-800"
            >
              <span className="font-semibold">Google</span>
              <span className="ml-1">로 계속하기</span>
            </Button>

            <Button
              variant="plain"
              fullWidth
              onClick={() => login('kakao')}
              className="py-3 text-base bg-[#FEE500] text-[#191919] hover:bg-[#f5dc00]"
            >
              <span className="font-semibold">Kakao</span>
              <span className="ml-1">로 계속하기</span>
            </Button>

            <Button
              variant="plain"
              fullWidth
              onClick={() => login('naver')}
              className="py-3 text-base bg-[#03C75A] text-white hover:bg-[#02b150]"
            >
              <span className="font-semibold">Naver</span>
              <span className="ml-1">로 계속하기</span>
            </Button>
          </div>
      </div>
    </div>
  )
}
