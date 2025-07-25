import { createFileRoute } from '@tanstack/react-router'
import CardDetail from '@/components/templates/CardDetail.tsx'
import ReviewList from '@/components/templates/ReviewList.tsx'
import StarRating from '@/components/templates/StarRating.tsx'
import StarRatingCount from '@/components/templates/StarRatingCount.tsx'

export const Route = createFileRoute('/items/$id')({
  component: ItemDetail,
})

function ItemDetail() {
  const { id } = Route.useParams()

  return (
    <div className="flex flex-col gap-8">
      <span>Home | Template | {id}</span>
      <CardDetail />
      <h3 className="font-bold">리뷰</h3>
      <div className="flex items-center gap-2">
        <StarRating />
        <StarRatingCount count={1000} />
      </div>
      <div className="w-200">
        <ReviewList />
      </div>
    </div>
  )
}