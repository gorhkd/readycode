import { RouterProvider } from 'react-router-dom'
import { router } from './router/router'
import { AppProviders } from './providers/AppProviders'

export function App() {
  return (
    <AppProviders>
      <RouterProvider router={router} />
    </AppProviders>
  )
}