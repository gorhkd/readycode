import StarRating from '@/components/templates/StarRating.tsx'
import ImagePlaceholder from '@/components/common/ImagePlaceholder.tsx'

export default function Card() {
  return (
    <div className="card bg-base-100 w-full overflow-hidden">
      <figure className="bg-base-200 aspect-[4/3] overflow-hidden">
        <ImagePlaceholder />
      </figure>
      <div className="card-body">
        <h2 className="card-title text-base-content">
          템플릿 제목
          <div className="badge badge-primary badge-sm">NEW</div>
        </h2>
        <p className="text-left text-base-content/70">Lorem ipsum dolor</p>
        <div className="flex items-center gap-1">
          <StarRating />
          <div className="badge badge-sm badge-ghost text-base-content/70">2024년 1월 1일</div>
        </div>
      </div>
    </div>
  )
}
