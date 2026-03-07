import { useEffect } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import { useTemplateDetailQuery } from '../features/templates/hooks/useTemplates'
import { Button } from '../shared/ui/Button'
import { getErrorMessage } from '../shared/api/apiError'

export function TemplateDetailModal() {
  const navigate = useNavigate()
  const { id } = useParams()
  const templateId = Number(id)

  const close = () => {
    if (window.history.length > 1) navigate(-1)
    else navigate('/templates', { replace: true })
  }

  useEffect(() => {
    const onKeyDown = (e: KeyboardEvent) => {
      if (e.key === 'Escape') close()
    }

    window.addEventListener('keydown', onKeyDown)
    return () => window.removeEventListener('keydown', onKeyDown)
  }, [])

  const query = useTemplateDetailQuery(
    Number.isFinite(templateId) && templateId > 0 ? templateId : -1
  )

  return (
    <div
      className="fixed inset-0 z-50 flex items-center justify-center bg-black/40 p-4 backdrop-blur-sm"
      onClick={close}
    >
      <div
        className="max-h-[85vh] w-full max-w-3xl overflow-y-auto rounded-3xl border border-zinc-200 bg-white p-6 shadow-xl dark:border-zinc-800 dark:bg-zinc-950"
        onClick={(e) => e.stopPropagation()}
      >
        <div className="flex items-start justify-between gap-3">
          <h2 className="text-xl font-semibold text-zinc-900 dark:text-zinc-100">
            템플릿 상세
          </h2>

          <Button variant="ghost" onClick={close}>
            닫기
          </Button>
        </div>

        <div className="mt-5">
          {query.status === 'pending' && (
            <div className="text-sm text-zinc-600 dark:text-zinc-300">로딩중...</div>
          )}

          {query.status === 'error' && (
            <div className="text-sm text-zinc-700 dark:text-zinc-200">
              에러: {getErrorMessage(query.error)}
            </div>
          )}

          {query.status === 'success' && query.data && (
            <div className="space-y-6">
              {/* 제목 + 가격 */}
              <div className="flex flex-col gap-3 border-b border-zinc-200 pb-4 dark:border-zinc-800 sm:flex-row sm:items-start sm:justify-between">
                <div className="min-w-0">
                  <div className="flex flex-wrap items-center gap-2">
                    <h3 className="text-2xl font-semibold text-zinc-900 dark:text-zinc-100">
                      {query.data.title}
                    </h3>

                    <span className="rounded-full border border-zinc-200 px-2 py-0.5 text-xs text-zinc-500 dark:border-zinc-800 dark:text-zinc-400">
                      {query.data.category}
                    </span>

                    {query.data.purchased && (
                      <span className="rounded-full bg-emerald-50 px-2 py-0.5 text-xs font-semibold text-emerald-700 dark:bg-emerald-950/40 dark:text-emerald-300">
                        구매 완료
                      </span>
                    )}
                  </div>

                  <div className="mt-2 flex flex-wrap items-center gap-3 text-sm text-zinc-600 dark:text-zinc-300">
                    <span>
                      ⭐ {query.data.avgRating} ({query.data.reviewCount})
                    </span>
                  </div>
                </div>

                <div className="shrink-0 text-left sm:text-right">
                  <div className="text-xs text-zinc-500 dark:text-zinc-400">가격</div>
                  <div className="mt-1 text-xl font-semibold text-zinc-900 dark:text-zinc-100">
                    {query.data.price === 0
                      ? '무료'
                      : `${query.data.price.toLocaleString()}P`}
                  </div>
                </div>
              </div>

              {/* 설명 */}
              <section className="space-y-2">
                <h4 className="text-sm font-semibold text-zinc-900 dark:text-zinc-100">
                  설명
                </h4>

                <div className="rounded-2xl border border-zinc-200 bg-zinc-50 p-4 dark:border-zinc-800 dark:bg-zinc-900">
                  {query.data.description ? (
                    <p className="text-sm leading-relaxed whitespace-pre-wrap text-zinc-700 dark:text-zinc-200">
                      {query.data.description}
                    </p>
                  ) : (
                    <p className="text-sm text-zinc-500 dark:text-zinc-400">
                      설명이 아직 등록되지 않았습니다.
                    </p>
                  )}
                </div>
              </section>

              {/* 액션 */}
              <div className="flex gap-2">
                <Button>다운로드</Button>
                <Button variant="ghost">문의 (나중)</Button>
              </div>
            </div>
          )}
        </div>
      </div>
    </div>
  )
}