import { MoonIcon, SunIcon } from '@phosphor-icons/react'
import { useEffect, useState } from 'react'

export default function ThemeToggleButton() {
  const [theme, setTheme] = useState('dark')

  useEffect(() => {
    document.documentElement.setAttribute('data-theme', theme)
  }, [theme])

  const toggleTheme = () => {
    setTheme((prev) => (prev === 'dark' ? 'bumblebee' : 'dark'))
  }

  return (
    <label className="swap swap-rotate">
      <input
        type="checkbox"
        onChange={toggleTheme}
        className="theme-controller"
        data-toggle-theme="dark,bumblebee"
        data-act-class="ACTIVE"
      />

      <SunIcon className="swap-on h-6 w-6" />
      <MoonIcon className="swap-off h-6 w-6" />
    </label>
  )
}
