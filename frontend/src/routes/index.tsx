import { Link, createFileRoute } from '@tanstack/react-router'
import { FadersHorizontalIcon } from '@phosphor-icons/react'
import Card from '@/components/templates/Card.tsx'

export const Route = createFileRoute('/')({
  component: Home,
})

function Home() {
  const cards = new Array(12).fill(1)
  const leftFilters = ['패치키 구조', '기능']
  const rightFilters = ['최신순', '인기순']

  return (
      <div className="flex flex-col items-center justify-center">
      <div className="flex justify-between w-full">
        <div className="flex justify-center items-center gap-1">
          <FadersHorizontalIcon size={20} />
          {leftFilters.map((word, i) => (
            <button key={i} className="btn btn-ghost">
              {word}
            </button>
          ))}
        </div>
        <div className="flex gap-1">
          {rightFilters.map((word, i) => (
            <button className="btn btn-ghost" key={i}>
              {word}
            </button>
          ))}
        </div>
      </div>

      <div className="divider m-0" />

      <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-8 w-full">
        {cards.map((_, i) => (
          <Link key={i} to={`items/${i}`}>
            <Card />
          </Link>
        ))}
      </div>
      </div>
  )
}
