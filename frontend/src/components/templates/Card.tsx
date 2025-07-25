import { StarHalfIcon, StarIcon } from '@phosphor-icons/react'
import logo from '@/logo.svg'

export default function Card() {
  return (
    <div className="card bg-base-100 w-full overflow-hidden">
      <figure className="bg-base-200 aspect-[4/3] overflow-hidden">
        <img
          src={logo}
          className="w-full h-full object-cover animate-[spin_20s_linear_infinite]"
          alt="logo"
        />
      </figure>
      <div className="card-body">
        <h2 className="card-title text-primary-content">
          템플릿 제목
          <div className="badge badge-primary badge-sm">NEW</div>
        </h2>
        <p className="text-left text-primary-content/70">Lorem ipsum dolor</p>
        <div className="card-actions">
          <div className="flex items-center gap-1">
            <div className="flex gap-0">
              <StarIcon size={14} weight="fill" className="fill-yellow-400" />
              <StarIcon size={14} weight="fill" className="fill-yellow-400" />
              <StarIcon size={14} weight="fill" className="fill-yellow-400" />
              <StarIcon size={14} weight="fill" className="fill-yellow-400" />
              <StarHalfIcon size={14} weight="fill" className="fill-yellow-400" />
            </div>
            <span className="text-primary-content/70">(1,000)</span>
          </div>
          <div className="badge badge-sm badge-ghost text-primary-content/70">2024년 1월 1일</div>
        </div>
      </div>
    </div>
  )
}
