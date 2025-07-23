import { EyeIcon, StarHalfIcon, StarIcon } from '@phosphor-icons/react'
import logo from '@/logo.svg'

export default function Card() {
  return (
    <div className="card bg-base-100 w-full shadow-sm">
      <figure className="bg-base-200 aspect-[4/3] overflow-hidden">
        <img
          src={logo}
          className="w-full h-full object-cover animate-[spin_20s_linear_infinite]"
          alt="logo"
        />
      </figure>
      <div className="card-body">
        <h2 className="card-title text-primary-content">
          Card Title
          <div className="badge badge-primary">NEW</div>
        </h2>
        <p className="text-left text-primary-content">짧은 설명글</p>
        <div className="card-actions">
          <div className="badge badge-soft badge-warning">
            <div className="flex gap-0">
              <StarIcon size={12} weight="fill" className="fill-yellow-400" />
              <StarIcon size={12} weight="fill" className="fill-yellow-400" />
              <StarIcon size={12} weight="fill" className="fill-yellow-400" />
              <StarIcon size={12} weight="fill" className="fill-yellow-400" />
              <StarHalfIcon size={12} weight="fill" className="fill-yellow-400" />
            </div>
            <span>(1,000)</span>
          </div>
          <div className="badge badge-outline badge-warning">
            <StarIcon size={12} weight="fill" className="fill-yellow-400" />
            <span>4.5</span>
          </div>
          <div className="badge badge-ghost">
            <EyeIcon size={12} />
            500
          </div>
        </div>
      </div>
    </div>
  )
}
