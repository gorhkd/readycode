import { createFileRoute } from '@tanstack/react-router'

export const Route = createFileRoute('/profile/deactivate')({
  component: ProfileDeactivate,
})

function ProfileDeactivate() {
  return <div>Hello "/profile/deactivate"!</div>
}
