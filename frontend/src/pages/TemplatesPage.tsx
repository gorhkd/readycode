import { Link } from 'react-router-dom'
import { useTemplatesInfiniteQuery } from '../features/templates/hooks/useTemplates'
import { getErrorMessage } from '../shared/api/apiError'
import { Button } from '../shared/ui/Button'
import reactLogo from '../assets/react.svg'

function PriceTag({ price }: { price: number }) {
  if (price === 0) {
    return (
      <span className="rounded-full bg-emerald-50 px-2.5 py-1 text-xs font-semibold text-emerald-700 dark:bg-emerald-950/40 dark:text-emerald-300">
        무료
      </span>
    )
  }
  return (
    <span className="rounded-full bg-zinc-100 px-2.5 py-1 text-xs font-semibold text-zinc-800 dark:bg-zinc-900 dark:text-zinc-200">
      {price.toLocaleString()}P
    </span>
  )
}

export function TemplatesPage() {
  const {
    data,
    status,
    error,
    refetch,
    fetchNextPage,
    hasNextPage,
    isFetchingNextPage,
  } = useTemplatesInfiniteQuery()

  if (status === 'pending') {
    return (
      <div className="rounded-2xl border border-zinc-200 bg-white p-6 text-sm text-zinc-600 dark:border-zinc-800 dark:bg-zinc-950 dark:text-zinc-300">
        로딩중...
      </div>
    )
  }

  if (status === 'error') {
    return (
      <div className="rounded-2xl border border-zinc-200 bg-white p-6 dark:border-zinc-800 dark:bg-zinc-950">
        <div className="text-sm text-zinc-700 dark:text-zinc-200">
          에러: {getErrorMessage(error)}
        </div>
        <div className="mt-3">
          <Button onClick={() => refetch()}>다시 시도</Button>
        </div>
      </div>
    )
  }

  const templates = data?.pages.flatMap((page) => page.templates) ?? []

  return (
    <div className="space-y-6">
      <div className="flex items-end justify-between gap-3">
        <div>
          <h1 className="text-2xl font-semibold tracking-tight">템플릿</h1>
          <p className="mt-1 text-sm text-zinc-600 dark:text-zinc-300">
            실무에서 바로 쓰는 뼈대들을 빠르게 꺼내 쓰자.
          </p>
        </div>

        <div className="text-xs text-zinc-500 dark:text-zinc-400">
          {templates.length}개 로드됨
        </div>
      </div>

      <ul className="grid grid-cols-1 gap-4 md:grid-cols-2">
        {templates.map((t) => (
          <li key={t.id}>
            <Link
              to={`/templates/${t.id}`}
              className="group block rounded-3xl border border-zinc-200 bg-white p-4 shadow-sm transition hover:-translate-y-0.5 hover:shadow-md dark:border-zinc-800 dark:bg-zinc-950"
            >
              {/* 1) 이미지(중간 사이즈) */}
              <div className="mb-3 rounded-2xl border border-zinc-200 bg-zinc-50 p-2 dark:border-zinc-800 dark:bg-zinc-900">
                <div className="aspect-16/10 w-full overflow-hidden rounded-xl bg-white dark:bg-zinc-950">
                  <img
                    src={reactLogo}
                    alt=""
                    className="h-full w-full object-contain p-1.5"
                  />
                </div>
              </div>

              {/* 2) 제목 + 가격 */}
              <div className="flex items-start justify-between gap-3">
                <div className="min-w-0">
                  <div className="truncate text-base font-semibold text-zinc-900 group-hover:underline dark:text-zinc-100">
                    {t.title}
                  </div>

                  {/* 3) 설명(임시) */}
                  <p className="mt-1 text-sm text-zinc-600 dark:text-zinc-300">
                    실무에서 바로 쓰는 뼈대 템플릿
                  </p>
                </div>

                <PriceTag price={t.price} />
              </div>

              {/* 4) 메타 */}
              <div className="mt-3 flex flex-wrap items-center justify-between gap-2 text-xs text-zinc-500 dark:text-zinc-400">
                <span className="rounded-full border border-zinc-200 px-2 py-0.5 dark:border-zinc-800">
                  {t.category}
                </span>
                <span>
                  ⭐ {t.avgRating} ({t.reviewCount})
                </span>
                <span>{new Date(t.createdAt).toLocaleDateString()}</span>
              </div>
            </Link>
          </li>
        ))}
      </ul>

      <div className="flex justify-center pt-2">
        <Button
          onClick={() => fetchNextPage()}
          disabled={!hasNextPage || isFetchingNextPage}
        >
          {isFetchingNextPage ? '불러오는 중...' : hasNextPage ? '더보기' : '마지막 페이지'}
        </Button>
      </div>
    </div>
  )
}
