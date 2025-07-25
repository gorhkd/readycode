import { StarHalfIcon, StarIcon } from '@phosphor-icons/react'

export default function StarRating() {
  return (
    <div className="flex gap-0">
      <StarIcon weight="fill" className="fill-yellow-400" />
      <StarIcon weight="fill" className="fill-yellow-400" />
      <StarIcon weight="fill" className="fill-yellow-400" />
      <StarIcon weight="fill" className="fill-yellow-400" />
      <StarHalfIcon weight="fill" className="fill-yellow-400" />
    </div>
  )
}
