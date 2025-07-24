import DrawerToggle from '@/components/common/navbar/DrawerToggle.tsx'
import DrawerToggleButton from '@/components/common/navbar/DrawerToggleButton.tsx'
import DrawerSidebar from '@/components/common/navbar/DrawerSidebar.tsx'
import { navLinks } from '@/components/common/navbar/navLinks.ts'
import { Link } from '@tanstack/react-router'
import ThemeToggleButton from '@/components/common/navbar/ThemeToggleButton.tsx'

export default function Navbar() {
  return (
    <header className="drawer fixed z-50">
      <DrawerToggle />
      <div className="drawer-content flex flex-col">
        <nav className="navbar navbar-custom bg-base-300 shadow-sm w-full">
          <div className="mx-auto w-full max-w-[1920px] px-4 flex items-center justify-between">
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
