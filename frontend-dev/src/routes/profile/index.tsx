import { createFileRoute } from '@tanstack/react-router'

export const Route = createFileRoute('/profile/')({
  component: Profile,
})

function Profile() {
  return <div>Hello "/profile"!</div>
}
