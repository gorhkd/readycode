import { ListIcon } from '@phosphor-icons/react'

export default function DrawerToggleButton() {
  return (
    <label htmlFor="my-drawer" aria-label="open sidebar" className="btn btn-square btn-ghost">
      <ListIcon />
    </label>
  )
}
