import Review from '@/components/templates/Review.tsx'

export default function ReviewList() {
  const reviews = new Array(5).fill(1)

  return (
    <ul className="list bg-base-100 py-2 rounded-box border border-base-content/10">
      <li className="p-4 pb-2 text-xs opacity-60 tracking-wide">최신순 &nbsp; | &nbsp; 조회순</li>
      {reviews.map((_, i) => (
        <Review key={i} />
      ))}
    </ul>
  )
}
