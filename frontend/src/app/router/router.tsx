import { createBrowserRouter, Navigate } from 'react-router-dom'
import { RootLayout } from '../layout/RootLayout'
import { GuidePage } from '../../pages/GuidePage'
import { LoginPage } from '../../pages/LoginPage'
import { NotFoundPage } from '../../pages/NotFoundPage'
import { OAuthCallbackPage } from '../../pages/OAuthCallbackPage'
import { SignupPage } from '../../pages/SignupPage'
import { RequireAuth } from './RequireAuth'
import { TemplatesLayout } from '../layout/TemplatesLayout'
import { TemplateDetailModal } from '../../pages/TemplateDetailModalPage'

export const router = createBrowserRouter([
  {
    path: '/',
    element: <RootLayout />,
    children: [
      { index: true, element: <Navigate to="/templates" replace /> },

      {
        path: 'templates',
        element: <TemplatesLayout />,
        children: [
          { path: ':id', element: <TemplateDetailModal /> },
        ],
      },

      { path: 'guide', element: <GuidePage /> },
      { path: 'login', element: <LoginPage /> },
      { path: 'oauth/:provider', element: <OAuthCallbackPage /> },
      { path: 'signup', element: <SignupPage /> },

      {
        path: 'me',
        element: (
          <RequireAuth>
            <div>내 정보 페이지(임시). 로그인 되어야만 보임.</div>
          </RequireAuth>
        ),
      },

      { path: '*', element: <NotFoundPage /> },
    ],
  },
])
