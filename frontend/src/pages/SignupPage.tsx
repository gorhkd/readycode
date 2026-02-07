import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { usersApi } from '../features/auth/api/usersApi'
import { authSession } from '../shared/auth/authSession'
import { Button } from '../shared/ui/Button'

type Purpose = 'LEARNING' | 'LECTURE'
type Role = 'USER' | 'ADMIN'

export function SignupPage() {
  const navigate = useNavigate()

  const [phoneNumber, setPhoneNumber] = useState('')
  const [nickname, setNickname] = useState('')
  const [purpose, setPurpose] = useState<Purpose>('LEARNING')

  const [userRole, setUserRole] = useState<Role>('USER')

  const [statusText, setStatusText] = useState<string | null>(null)
  const [loading, setLoading] = useState(false)

  const normalizePhone = (v: string) => v.replace(/[^\d]/g, '')

  const onSubmit = async () => {
    const pn = normalizePhone(phoneNumber)
    const nn = nickname.trim()

    if (!pn || pn.length < 10) {
      setStatusText('전화번호를 확인해줘. (숫자만, 10~11자리)')
      return
    }
    if (!nn || nn.length < 2) {
      setStatusText('닉네임은 2글자 이상으로 해줘.')
      return
    }

    const tempAccessToken = authSession.getAccessToken()
    if (!tempAccessToken) {
      setStatusText('임시 토큰이 없어. 다시 로그인부터 해줘.')
      return
    }

    setLoading(true)
    setStatusText(null)

    try {
      const token = await usersApi.signup(tempAccessToken, {
        phoneNumber: pn,
        nickname: nn,
        purpose,
        userRole, // ✅ 선택값 그대로 보냄
      })

      authSession.setAccessToken(token.accessToken)
      if (token.refreshToken) authSession.setRefreshToken(token.refreshToken)

      navigate('/templates', { replace: true })
    } catch (e: any) {
      setStatusText(`회원가입 실패: ${e?.message ?? '알 수 없음'}`)
    } finally {
      setLoading(false)
    }
  }

  const ToggleCard = ({
    active,
    onClick,
    title,
    desc,
  }: {
    active: boolean
    onClick: () => void
    title: string
    desc: string
  }) => (
    <button
      type="button"
      onClick={onClick}
      className={[
        'w-full rounded-2xl border px-4 py-3 text-left transition',
        active
          ? 'border-zinc-900 bg-zinc-900 text-white dark:border-zinc-200 dark:bg-white dark:text-zinc-900'
          : 'border-zinc-200 bg-white text-zinc-900 hover:bg-zinc-50 dark:border-zinc-800 dark:bg-zinc-950 dark:text-zinc-100 dark:hover:bg-zinc-900',
      ].join(' ')}
    >
      <div className="text-sm font-semibold">{title}</div>
      <div
        className={
          active
            ? 'mt-1 text-xs text-white/80 dark:text-zinc-700'
            : 'mt-1 text-xs text-zinc-500 dark:text-zinc-400'
        }
      >
        {desc}
      </div>
    </button>
  )

  const RoleButton = ({ value }: { value: Role }) => {
    const active = userRole === value
    return (
      <button
        type="button"
        onClick={() => setUserRole(value)}
        className={[
          'w-full rounded-xl border px-3 py-2 text-sm font-semibold transition',
          active
            ? 'border-zinc-900 bg-zinc-900 text-white dark:border-zinc-200 dark:bg-white dark:text-zinc-900'
            : 'border-zinc-200 bg-white text-zinc-800 hover:bg-zinc-50 dark:border-zinc-800 dark:bg-zinc-950 dark:text-zinc-100 dark:hover:bg-zinc-900',
        ].join(' ')}
      >
        {value === 'USER' ? '일반 사용자(USER)' : '관리자(ADMIN)'}
      </button>
    )
  }

  return (
    <div className="mx-auto mt-10 w-full max-w-lg">
      <div className="rounded-3xl border border-zinc-200 bg-white p-8 shadow-sm dark:border-zinc-800 dark:bg-zinc-950">
        <div className="mb-6">
          <h1 className="text-2xl font-semibold">추가 정보 입력</h1>
          <p className="mt-2 text-sm text-zinc-600 dark:text-zinc-300">
            소셜 로그인 후, 서비스 이용을 위해 최소 정보만 받아.
          </p>
        </div>

        {/* 목적 토글 */}
        <div className="mb-6">
          <div className="mb-2 text-sm font-medium">사용 목적</div>
          <div className="grid grid-cols-1 gap-3 sm:grid-cols-2">
            <ToggleCard
              active={purpose === 'LEARNING'}
              onClick={() => setPurpose('LEARNING')}
              title="학습용"
              desc="혼자 빠르게 뼈대 가져와서 공부"
            />
            <ToggleCard
              active={purpose === 'LECTURE'}
              onClick={() => setPurpose('LECTURE')}
              title="강의용"
              desc="교육/강의 자료로 정리해서 사용"
            />
          </div>
        </div>

        {/* ✅ Role 선택: 무조건 노출 */}
        <div className="mb-6">
          <div className="mb-2 text-sm font-medium">권한(Role)</div>
          <div className="grid grid-cols-1 gap-2 sm:grid-cols-2">
            <RoleButton value="USER" />
            <RoleButton value="ADMIN" />
          </div>
        </div>

        {/* 입력 폼 */}
        <div className="space-y-4">
          <div>
            <label className="mb-1 block text-sm font-medium">전화번호</label>
            <input
              value={phoneNumber}
              onChange={(e) => setPhoneNumber(e.target.value)}
              inputMode="numeric"
              placeholder="01012345678"
              className="w-full rounded-2xl border border-zinc-200 bg-white px-4 py-3 text-sm outline-none focus:border-zinc-900 dark:border-zinc-800 dark:bg-zinc-950 dark:text-zinc-100 dark:focus:border-zinc-200"
            />
          </div>

          <div>
            <label className="mb-1 block text-sm font-medium">닉네임</label>
            <input
              value={nickname}
              onChange={(e) => setNickname(e.target.value)}
              placeholder="예: 파란"
              className="w-full rounded-2xl border border-zinc-200 bg-white px-4 py-3 text-sm outline-none focus:border-zinc-900 dark:border-zinc-800 dark:bg-zinc-950 dark:text-zinc-100 dark:focus:border-zinc-200"
            />
          </div>

          {statusText && (
            <div className="rounded-2xl border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-700 dark:border-red-900/40 dark:bg-red-950/40 dark:text-red-200">
              {statusText}
            </div>
          )}

          <div className="pt-1">
            <Button fullWidth onClick={onSubmit} disabled={loading}>
              {loading ? '처리중...' : '회원가입 완료'}
            </Button>
          </div>
        </div>
      </div>
    </div>
  )
}
