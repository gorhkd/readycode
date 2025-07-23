import { createFileRoute } from '@tanstack/react-router'
import Card from '@/components/templates/Card.tsx'

export const Route = createFileRoute('/')({
  component: Home,
})

function Home() {
  const cards = new Array(12).fill(1)
  return (
    <div className="text-center">
      <main className="min-h-screen flex flex-col items-center justify-center m-auto max-w-[1600px]">
        <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-4 w-full">
          {cards.map((_, i) => (
            <Card key={i} />
          ))}
        </div>
      </main>
    </div>
  )
}
