import { createFileRoute } from '@tanstack/react-router'

export const Route = createFileRoute('/profile/edit')({
  component: ProfileEdit,
})

function ProfileEdit() {
  return <div>Hello "/profile/edit"!</div>
}
