import logo from '@/logo.svg'

export default function ImagePlaceholder() {
  return (
    <img
      src={logo}
      className="w-full h-full object-cover animate-[spin_20s_linear_infinite]"
      alt="logo"
    />
  )
}
