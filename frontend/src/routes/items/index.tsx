import { createFileRoute } from '@tanstack/react-router'

export const Route = createFileRoute('/items/')({
  component: Items,
})

function Items() {
  return <div>Hello "/items"!</div>
}
