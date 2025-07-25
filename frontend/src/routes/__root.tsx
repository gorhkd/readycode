import { createRootRoute, Outlet } from '@tanstack/react-router'
import Navbar from '@/components/common/navbar/Navbar.tsx'
import Footer from '@/components/common/Footer.tsx'

export const Route = createRootRoute({
  component: RootComponent,
})

function RootComponent() {
  return (
    <div className="flex flex-col min-h-screen">
      <Navbar />

      <main className="flex-grow lg:px-4 px-1 pt-18 m-auto max-w-[1600px]">
        <Outlet />
      </main>

      <Footer />
    </div>
  )
}
