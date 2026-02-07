import { createFileRoute } from '@tanstack/react-router'

export const Route = createFileRoute('/items/$id')({
  component: ItemDetail,
})

function ItemDetail() {
  const { id } = Route.useParams()
  return <div>Hello "/items/ {id}"!</div>
}
