import { createFileRoute } from '@tanstack/react-router'
import SocialButton from '@/components/login/SocialButton'

export const Route = createFileRoute('/login')({
  component: Login,
})

function Login() {
  const onClick = () => {}
  return (
    <div className="flex w-full flex-1 flex-col items-center justify-center gap-4">
      <SocialButton provider="naver" label="네이버 로그인" onClick={onClick} />
      <SocialButton provider="google" label="Google 로그인" onClick={onClick} />
      <SocialButton provider="kakao" label="카카오 로그인" onClick={onClick} />
    </div>
  )
}
