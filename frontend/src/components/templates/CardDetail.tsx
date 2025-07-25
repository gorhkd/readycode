import ImageCarousel from '@/components/templates/ImageCarousel.tsx'
import StarRating from '@/components/templates/StarRating.tsx'
import { HeartIcon } from '@phosphor-icons/react'
import { dummyText } from '@/components/common/dummyText.ts'

export default function CardDetail() {
  return (
    <div className="grid [grid-template-columns:3fr_2fr] gap-16 px-8 py-16 rounded-box border border-base-300">
      <ImageCarousel />

      <div className="flex flex-col flex-1 gap-6">
        <StarRating />
        <div className="flex flex-col gap-2">
          <h2 className="text-xl font-bold">템플릿 제목</h2>
          <p className="text-base-content/40 text-sm">2024년 1월 1일</p>
        </div>
        <p className="text-base-content/80 leading-[1.75]">{dummyText}</p>

        <div className="flex gap-1">
          <button className="btn btn-wide btn-success">구매</button>
          <button className="btn btn-error">
            <HeartIcon className="text-xl" />
          </button>
        </div>

        <div className="divider m-0" />

        <div className="flex flex-col gap-4">
          <h3 className="font-bold">기술 스택</h3>
          <ul className="list-disc pl-4 text-base-content/70">
            <li className="pl-2">React</li>
            <li className="pl-2">React</li>
            <li className="pl-2">React</li>
          </ul>
        </div>
      </div>
    </div>
  )
}
