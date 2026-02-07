import type { ButtonHTMLAttributes } from 'react'

type Props = ButtonHTMLAttributes<HTMLButtonElement> & {
  variant?: 'primary' | 'ghost' | 'plain'
  fullWidth?: boolean
}

export function Button({
  variant = 'primary',
  fullWidth,
  className,
  ...props
}: Props) {
  const base =
    'inline-flex items-center justify-center rounded-xl px-4 py-2 text-sm font-medium transition disabled:opacity-50 disabled:pointer-events-none'

  const variants: Record<NonNullable<Props['variant']>, string> = {
    primary: 'bg-zinc-900 text-white hover:bg-zinc-800',
    ghost:
      'bg-transparent text-zinc-700 hover:bg-zinc-100 dark:text-zinc-200 dark:hover:bg-zinc-800',
    plain: '',
  }

  const cls = `${base} ${variants[variant]} ${fullWidth ? 'w-full' : ''} ${className ?? ''}`

  return <button className={cls} {...props} />
}
