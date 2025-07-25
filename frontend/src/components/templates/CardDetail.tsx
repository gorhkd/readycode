import ImageCarousel from '@/components/templates/ImageCarousel.tsx'
import StarRating from '@/components/templates/StarRating.tsx'
import { HeartIcon } from '@phosphor-icons/react'
import { dummyText } from '@/components/common/dummyText.ts'
import StarRatingCount from '@/components/templates/StarRatingCount.tsx'

export default function CardDetail() {
  return (
    <div className="grid [grid-template-columns:3fr_2fr] gap-16 px-8 py-24 rounded-box border border-base-content/10">
      <ImageCarousel />

      <div className="flex flex-col flex-1 gap-8">
        <div className="flex flex-col gap-2">
          <h2 className="text-3xl font-bold">템플릿 제목</h2>
          <div className="flex items-center gap-4">
            <div className="flex items-center gap-2">
              <span>4.5</span>
              <StarRating />
              <StarRatingCount count={1000} />
            </div>
          </div>
          <p className="text-base-content/40 text-sm">2024년 1월 1일</p>
        </div>
        <p className="text-base-content/80 leading-[1.75]">{dummyText}</p>

        <div className="flex gap-1">
          <button className="btn btn-wide btn-lg btn-success">구매</button>
          <button className="btn btn-error btn-lg btn-ghost">
            <HeartIcon className="text-xl" />
          </button>
        </div>

        <div className="divider m-0" />

        <div className="flex flex-col gap-4">
          <h3 className="font-bold text-lg">기술 스택</h3>
          <ul className="flex flex-col gap-2 list-disc pl-4 text-base-content/80">
            <li className="pl-2">{dummyText.slice(0, 12)}</li>
            <li className="pl-2">{dummyText.slice(13, 82)}</li>
            <li className="pl-2">{dummyText.slice(83, 182)}</li>
          </ul>
        </div>

        <div className="divider m-0" />

        <div className="flex flex-col gap-4">
          <h3 className="font-bold text-lg">기타 정보</h3>
          <span className="text-base-content/80">{dummyText.slice(0, 110)}</span>
        </div>
      </div>
    </div>
  )
}
