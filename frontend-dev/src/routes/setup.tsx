import { createFileRoute } from '@tanstack/react-router'

export const Route = createFileRoute('/setup')({
  component: Setup,
})

function Setup() {
  return <div>Hello "/setup"!</div>
}
