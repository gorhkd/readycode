import { Link } from 'react-router-dom'

type TemplateLike = {
  id: number
  title: string
  price: number
  category?: string
  avgRating?: string
  reviewCount?: number
}

type Props = {
  t: TemplateLike
}

export function TemplateCard({ t }: Props) {
  const isFree = t.price === 0

  return (
    <li className="h-full">
      <Link
        to={`/templates/${t.id}`}
        className="group block h-full rounded-2xl border border-zinc-200 bg-white p-4 shadow-sm transition
                   hover:-translate-y-0.5 hover:shadow-md
                   focus:outline-none focus:ring-2 focus:ring-zinc-400/30
                   dark:border-zinc-800 dark:bg-zinc-900"
      >
        <div className="flex items-start justify-between gap-3">
          <h3 className="text-sm font-semibold leading-5 text-zinc-900 group-hover:text-black dark:text-zinc-100">
            {t.title}
          </h3>

          <span
            className={
              'shrink-0 rounded-full px-2 py-0.5 text-xs font-semibold ' +
              (isFree
                ? 'bg-emerald-50 text-emerald-700 dark:bg-emerald-900/30 dark:text-emerald-200'
                : 'bg-zinc-100 text-zinc-800 dark:bg-zinc-800 dark:text-zinc-100')
            }
          >
            {isFree ? 'FREE' : `${t.price}P`}
          </span>
        </div>

        {(t.category || t.avgRating || typeof t.reviewCount === 'number') && (
          <div className="mt-3 flex flex-wrap items-center gap-2 text-xs text-zinc-600 dark:text-zinc-300">
            {t.category && (
              <span className="rounded-full bg-zinc-100 px-2 py-0.5 text-zinc-700 dark:bg-zinc-800 dark:text-zinc-200">
                {t.category}
              </span>
            )}
            {t.avgRating && <span>★ {t.avgRating}</span>}
            {typeof t.reviewCount === 'number' && <span>({t.reviewCount})</span>}
          </div>
        )}

        <div className="mt-5 text-xs text-zinc-500 dark:text-zinc-400">
          자세히 보기 →
        </div>
      </Link>
    </li>
  )
}
