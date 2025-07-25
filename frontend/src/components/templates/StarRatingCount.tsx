interface StarRatingCountProps {
  count: number
}

export default function StarRatingCount({ count }: StarRatingCountProps) {
  return <span className="text-primary-content/70">({count})</span>
}
