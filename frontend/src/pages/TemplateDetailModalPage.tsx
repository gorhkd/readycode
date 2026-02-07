import { useEffect } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import { useTemplateDetailQuery } from '../features/templates/hooks/useTemplates'
import { Button } from '../shared/ui/Button'
import { getErrorMessage } from '../shared/api/apiError'
import reactLogo from '../assets/react.svg'

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
  })

  const query = useTemplateDetailQuery(
    Number.isFinite(templateId) && templateId > 0 ? templateId : -1
  )

  return (
    <div
      className="fixed inset-0 z-50 flex items-center justify-center bg-black/40 p-4 backdrop-blur-sm"
      onClick={close}
    >
      <div
        className="w-full max-w-3xl max-h-[85vh] overflow-y-auto rounded-3xl border border-zinc-200 bg-white p-6 shadow-xl dark:border-zinc-800 dark:bg-zinc-950"
        onClick={(e) => e.stopPropagation()}
      >
        <div className="flex items-start justify-between gap-3">
          <div>
            <h2 className="text-xl font-semibold">템플릿 상세</h2>
            <p className="mt-1 text-sm text-zinc-500 dark:text-zinc-400">
              바깥 클릭 또는 ESC로 닫기
            </p>
          </div>
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
            <div className="space-y-4">
              {/* 이미지 프레임 */}
              <div className="rounded-2xl border border-zinc-200 bg-zinc-50 p-2 dark:border-zinc-800 dark:bg-zinc-900">
                <div className="aspect-[16/10] w-full overflow-hidden rounded-xl bg-white dark:bg-zinc-950">
                  <img
                    src={reactLogo}
                    alt=""
                    className="h-full w-full object-contain p-2"
                  />
                </div>
              </div>

              <div className="text-2xl font-semibold">{query.data.title}</div>

              <div className="flex flex-wrap items-center gap-2 text-sm text-zinc-600 dark:text-zinc-300">
                <span className="rounded-full border border-zinc-200 px-2 py-0.5 dark:border-zinc-800">
                  {query.data.category}
                </span>
                <span>
                  ⭐ {query.data.avgRating} ({query.data.reviewCount})
                </span>
                <span className="ml-auto font-semibold text-zinc-900 dark:text-zinc-100">
                  {query.data.price === 0 ? '무료' : `${query.data.price.toLocaleString()}P`}
                </span>
              </div>

              {query.data.description && (
                <p className="text-sm leading-relaxed text-zinc-700 dark:text-zinc-200">
                  {query.data.description}
                </p>
              )}

              <div className="flex gap-2">
                {query.data.purchased ? (
                  <Button>다운로드 (2차)</Button>
                ) : (
                  <Button>구매하기 (2차)</Button>
                )}
                <Button variant="ghost">문의 (나중)</Button>
              </div>
            </div>
          )}
        </div>
      </div>
    </div>
  )
}
