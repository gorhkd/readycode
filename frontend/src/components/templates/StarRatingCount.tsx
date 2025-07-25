interface StarRatingCountProps {
  count: number
}

export default function StarRatingCount({ count }: StarRatingCountProps) {
    return <span className="text-base-content/40">({count})</span>
}
