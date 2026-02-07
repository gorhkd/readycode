import { navLinks } from '@/components/common/navbar/navLinks.ts'
import { Link } from '@tanstack/react-router'

export default function DrawerSidebar() {
  return (
    <div className="drawer-side">
      <label htmlFor="my-drawer" aria-label="close sidebar" className="drawer-overlay"></label>
      <ul className="menu bg-base-200 min-h-full w-80 p-4">
        {navLinks.map((link) => (
          <li>
            <Link to={link.url}>{link.name}</Link>
          </li>
        ))}
      </ul>
    </div>
  )
}
