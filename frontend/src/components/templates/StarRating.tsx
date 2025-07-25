import { StarHalfIcon, StarIcon } from '@phosphor-icons/react'

export default function StarRating() {
  return (
    <div className="flex items-center gap-1">
      <div className="flex gap-0">
        <StarIcon size={14} weight="fill" className="fill-yellow-400" />
        <StarIcon size={14} weight="fill" className="fill-yellow-400" />
        <StarIcon size={14} weight="fill" className="fill-yellow-400" />
        <StarIcon size={14} weight="fill" className="fill-yellow-400" />
        <StarHalfIcon size={14} weight="fill" className="fill-yellow-400" />
      </div>
      <span className="text-primary-content/70">(1,000)</span>
    </div>
  )
}
