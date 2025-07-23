import { MoonIcon, SunIcon } from '@phosphor-icons/react'

export default function ThemeToggleButton() {
  return (
    <label className="swap swap-rotate">
      <input type="checkbox" className="theme-controller" value="nord" />

      <SunIcon className="swap-on h-6 w-6" />
      <MoonIcon className="swap-off h-6 w-6" />
    </label>
  )
}
