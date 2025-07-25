import StarRating from '@/components/templates/StarRating.tsx'
import ImagePlaceholder from '@/components/common/ImagePlaceholder.tsx'
import { ThumbsDownIcon, ThumbsUpIcon } from '@phosphor-icons/react'
import { dummyText } from '@/components/common/dummyText.ts'

export default function Review() {
  return (
    <li className="list-row">
      <div className="size-10 rounded-box">
        <ImagePlaceholder />
      </div>
      <div>
        <div>Dio Lupa</div>
        <StarRating />
        <div className="text-xs uppercase font-light text-primary-content/60">2024년 1월 1 월</div>
      </div>
      <p className="list-col-wrap text-xs">{dummyText}</p>
      <button className="btn btn-square btn-ghost">
        <ThumbsUpIcon />
      </button>
      <button className="btn btn-square btn-ghost">
        <ThumbsDownIcon />
      </button>
      <button className="btn btn-square btn-ghost font-light text-primary-content/40">신고</button>
    </li>
  )
}
