import { Link } from 'react-router-dom'
import { useTemplatesInfiniteQuery } from '../features/templates/hooks/useTemplates'
import { getErrorMessage } from '../shared/api/apiError'
import { Button } from '../shared/ui/Button'

function PriceTag({ price }: { price: number }) {
  if (price === 0) {
    return (
      <span className="inline-flex items-center rounded-full bg-emerald-50 px-2 py-0.5 text-[11px] font-semibold text-emerald-700 dark:bg-emerald-950/40 dark:text-emerald-300">
        무료
      </span>
    )
  }

  return (
    <span className="inline-flex items-center rounded-full bg-zinc-100 px-2 py-0.5 text-[11px] font-semibold text-zinc-800 dark:bg-zinc-900 dark:text-zinc-200">
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
      <div className="rounded-xl border border-zinc-200 bg-white p-5 text-sm text-zinc-600 dark:border-zinc-800 dark:bg-zinc-950 dark:text-zinc-300">
        로딩중...
      </div>
    )
  }

  if (status === 'error') {
    return (
      <div className="rounded-xl border border-zinc-200 bg-white p-5 dark:border-zinc-800 dark:bg-zinc-950">
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
    <div className="space-y-5">
      <div className="flex items-end justify-between gap-3">
        <div className="space-y-1">
          <h1 className="text-2xl font-semibold tracking-tight text-zinc-900 dark:text-zinc-100">
            템플릿
          </h1>
          <p className="text-sm text-zinc-600 dark:text-zinc-300">
            초기 프로젝트와 학습용으로 바로 가져다 쓸 수 있는 백엔드 코드 템플릿
          </p>
        </div>

        <div className="shrink-0 rounded-full bg-zinc-100 px-2.5 py-1 text-[11px] font-medium text-zinc-600 dark:bg-zinc-900 dark:text-zinc-300">
          {templates.length}개 로드됨
        </div>
      </div>

      <ul className="grid grid-cols-1 gap-6 md:grid-cols-2">
        {templates.map((t) => (
          <li key={t.id}>
            <Link
              to={`/templates/${t.id}`}
              className="group block h-full rounded-xl border border-zinc-200 bg-white px-4 py-3.5 shadow-sm transition hover:border-zinc-300 hover:bg-zinc-50 hover:shadow dark:border-zinc-800 dark:bg-zinc-950 dark:hover:border-zinc-700 dark:hover:bg-zinc-900/60"
            >
              <div className="flex h-full items-start justify-between gap-3">
                <div className="min-w-0 flex-1">
                  <div className="flex flex-wrap items-center gap-2">
                    <h2 className="truncate text-[15px] font-semibold text-zinc-900 group-hover:underline dark:text-zinc-100">
                      {t.title}
                    </h2>

                    <span className="shrink-0 rounded-md border border-zinc-200 px-1.5 py-0.5 text-[10px] font-medium text-zinc-500 dark:border-zinc-700 dark:text-zinc-400">
                      {t.category}
                    </span>
                  </div>

                  <div className="mt-3 flex flex-wrap items-center gap-2 text-[12px] text-zinc-500 dark:text-zinc-400">
                    <span className="font-medium text-amber-600 dark:text-amber-400">
                      ★ {t.avgRating}
                    </span>
                    <span>리뷰 {t.reviewCount}개</span>
                    <span className="text-zinc-300 dark:text-zinc-700">•</span>
                    <span>{new Date(t.createdAt).toLocaleDateString()}</span>
                  </div>
                </div>

                <div className="shrink-0 pt-0.5">
                  <PriceTag price={t.price} />
                </div>
              </div>
            </Link>
          </li>
        ))}
      </ul>

      <div className="flex justify-center pt-1">
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