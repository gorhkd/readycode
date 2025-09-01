import { Link } from '@tanstack/react-router'
import DrawerToggle from '@/components/common/navbar/DrawerToggle.tsx'
import DrawerToggleButton from '@/components/common/navbar/DrawerToggleButton.tsx'
import DrawerSidebar from '@/components/common/navbar/DrawerSidebar.tsx'
import { navLinks } from '@/components/common/navbar/navLinks.ts'
import ThemeToggleButton from '@/components/common/navbar/ThemeToggleButton.tsx'

export default function Navbar() {
  return (
    <header className="drawer sticky top-0 z-50">
      <DrawerToggle />
      <div className="drawer-content flex flex-col">
        <nav className="navbar bg-base-300 w-full shadow-sm">
          <div className="mx-auto flex w-full max-w-[1920px] items-center justify-between px-4">
            <div className="flex-none lg:hidden">
              <DrawerToggleButton />
            </div>

            <div className="flex-1">
              <Link to="/" className="btn btn-ghost text-xl">
                Ready Code
              </Link>
            </div>

            <div className="hidden flex-none lg:block">
              <ThemeToggleButton />
              <ul className="menu menu-horizontal">
                {navLinks.map((link) => (
                  <li>
                    <Link to={link.url}>{link.name}</Link>
                  </li>
                ))}
              </ul>
            </div>
          </div>
        </nav>
      </div>
      <DrawerSidebar />
    </header>
  )
}
