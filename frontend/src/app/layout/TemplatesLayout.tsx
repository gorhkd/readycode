import { Outlet, useParams } from 'react-router-dom'
import { useEffect } from 'react'
import { TemplatesPage } from '../../pages/TemplatesPage'

export function TemplatesLayout() {
  const { id } = useParams()
  const modalOpen = !!id

  useEffect(() => {
    if (!modalOpen) return

    const scrollY = window.scrollY

    const prevOverflow = document.body.style.overflow
    const prevPosition = document.body.style.position
    const prevTop = document.body.style.top
    const prevWidth = document.body.style.width

    document.body.style.overflow = 'hidden'
    document.body.style.position = 'fixed'
    document.body.style.top = `-${scrollY}px`
    document.body.style.width = '100%'

    return () => {
      document.body.style.overflow = prevOverflow
      document.body.style.position = prevPosition
      document.body.style.top = prevTop
      document.body.style.width = prevWidth
      window.scrollTo(0, scrollY)
    }
  }, [modalOpen])

  return (
    <div className="relative">
      <div
        className={[
          'transition',
          modalOpen ? 'blur-sm brightness-95 pointer-events-none' : '',
        ].join(' ')}
      >
        <TemplatesPage />
      </div>
      <Outlet />
    </div>
  )
}