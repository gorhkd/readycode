import { Outlet, createRootRoute } from '@tanstack/react-router'
import Navbar from '@/components/common/navbar/Navbar.tsx'
import Footer from '@/components/common/Footer.tsx'

export const Route = createRootRoute({
  component: RootComponent,
})

function RootComponent() {
  return (
    <div className="flex min-h-screen flex-col">
      <Navbar />

      <main className="flex flex-grow p-1">
        <Outlet />
      </main>

      <Footer />
    </div>
  )
}
