import type { PropsWithChildren } from 'react'
import { Navigate, useLocation } from 'react-router-dom'
import { authSession } from '../../shared/auth/authSession'

export function RequireAuth({ children }: PropsWithChildren) {
  const location = useLocation()
  const loggedIn = authSession.isLoggedIn()

  if (!loggedIn) {
    return <Navigate to="/login" replace state={{ from: location.pathname }} />
  }

  return <>{children}</>
}
