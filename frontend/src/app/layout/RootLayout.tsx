import { NavLink, Outlet, useNavigate } from 'react-router-dom'
import { authSession } from '../../shared/auth/authSession'
import { httpClient } from '../../shared/api/httpClient'
import { Button } from '../../shared/ui/Button'

export function RootLayout() {
  const navigate = useNavigate()
  const loggedIn = authSession.isLoggedIn()

  const onLogout = async () => {
    const refreshToken = authSession.getRefreshToken()

    try {
      if (refreshToken) {
        await httpClient.post('/api/auth/logout', { refreshToken })
      }
    } finally {
      authSession.clear()
      navigate('/templates', { replace: true })
    }
  }

  return (
    <div className="min-h-screen bg-white text-zinc-900 dark:bg-zinc-950 dark:text-zinc-100">
      <header className="sticky top-0 z-10 border-b border-zinc-200 bg-white/80 backdrop-blur dark:border-zinc-800 dark:bg-zinc-950/80">
        <div className="mx-auto flex max-w-5xl items-center gap-3 px-4 py-3">
          <NavLink to="/templates" className="text-sm font-semibold">
            ReadyCode
          </NavLink>

          <NavLink to="/guide" className="text-sm text-zinc-600 hover:text-zinc-900 dark:text-zinc-300 dark:hover:text-white">
            소개/가이드
          </NavLink>

          <div className="ml-auto flex items-center gap-2">
            <span className="text-xs text-zinc-500 dark:text-zinc-400">
              {loggedIn ? '로그인됨' : '비로그인'}
            </span>

            {loggedIn ? (
              <Button variant="ghost" onClick={onLogout}>로그아웃</Button>
            ) : (
              <Button onClick={() => navigate('/login')}>로그인</Button>
            )}
          </div>
        </div>
      </header>

      <main className="mx-auto max-w-5xl px-4 py-6">
        <Outlet />
      </main>
    </div>
  )
}
