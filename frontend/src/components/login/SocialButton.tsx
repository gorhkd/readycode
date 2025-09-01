import { cva } from 'class-variance-authority'
import { cn } from '@/config/cn'

type SocialProvider = 'naver' | 'google' | 'kakao'

type SocialButtonProps = {
  provider: SocialProvider
  label: string
  className?: string
  onClick?: (provider: SocialProvider) => void
}

const socialBtn = cva('btn border-none w-3xs', {
  variants: {
    provider: {
      naver: 'bg-[#03C75A] text-white',
      google: 'bg-white text-gray-800',
      kakao: 'bg-[#FEE500] text-gray-800',
    },
  },
})

export default function SocialButton({ provider, label, className, onClick }: SocialButtonProps) {
  return (
    <button
      type="button"
      className={cn(socialBtn({ provider }), className)}
      onClick={() => onClick?.(provider)}
    >
      {label}
    </button>
  )
}
