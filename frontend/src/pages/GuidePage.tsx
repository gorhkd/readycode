import { useNavigate } from 'react-router-dom'
import { Button } from '../shared/ui/Button'
import { authSession } from '../shared/auth/authSession'

export function GuidePage() {
  const navigate = useNavigate()
  const loggedIn = authSession.isLoggedIn()

  return (
    <div className="space-y-10">
      <section className="rounded-3xl border border-zinc-200 bg-white p-8 shadow-sm dark:border-zinc-800 dark:bg-zinc-950">
        <div className="flex flex-col gap-4 md:flex-row md:items-center md:justify-between">
          <div className="space-y-3">
            <p className="text-sm text-zinc-500 dark:text-zinc-400">ReadyCode</p>
            <h1 className="text-3xl font-semibold tracking-tight">
              필요한 백엔드 뼈대를 빠르게 꺼내 쓰는 템플릿 마켓
            </h1>
            <p className="max-w-2xl text-zinc-600 dark:text-zinc-300">
              로그인/권한, 게시판, 파일 업로드, 예외 처리 같은 반복 작업을 “프로덕션 기준”으로 정리해둔 템플릿을 제공합니다.
              너는 비즈니스 로직과 개선에 시간을 쓰면 돼.
            </p>
          </div>

          <div className="flex gap-2">
            <Button onClick={() => navigate('/templates')}>템플릿 둘러보기</Button>
            {!loggedIn ? (
              <Button variant="ghost" onClick={() => navigate('/login')}>
                로그인
              </Button>
            ) : (
              <Button variant="ghost" onClick={() => navigate('/me')}>
                내 정보
              </Button>
            )}
          </div>
        </div>

        <div className="mt-6 flex flex-wrap gap-2">
          {[
            'Spring Boot 기준',
            '실무 폴더 구조',
            '에러/응답 포맷 통일',
            '확장 가능한 설계',
            '읽기 쉬운 코드',
          ].map((t) => (
            <span
              key={t}
              className="rounded-full border border-zinc-200 px-3 py-1 text-xs text-zinc-600 dark:border-zinc-800 dark:text-zinc-300"
            >
              {t}
            </span>
          ))}
        </div>
      </section>

      <section className="grid gap-4 md:grid-cols-3">
        <Card
          title="시간 절약"
          desc="반복 구현(인증/CRUD/예외/문서화)을 줄이고 핵심 기능에 집중."
        />
        <Card
          title="구조가 남는다"
          desc="패키지 구조, 계층, 흐름이 정리된 상태로 시작해서 유지보수 난이도↓."
        />
        <Card
          title="확장 전제"
          desc="나중에 결제/다운로드/리뷰/관리자 기능 붙이기 쉬운 형태로 설계."
        />
      </section>

      <section className="rounded-3xl border border-zinc-200 bg-white p-8 dark:border-zinc-800 dark:bg-zinc-950">
        <h2 className="text-xl font-semibold">사용 흐름</h2>
        <p className="mt-2 text-sm text-zinc-600 dark:text-zinc-300">
          MVP 기준으로 “이렇게 쓰는 서비스”라는 감을 주는 섹션.
        </p>

        <ol className="mt-6 grid gap-4 md:grid-cols-3">
          <Step
            no="1"
            title="템플릿 탐색"
            desc="목록에서 가격/카테고리/평점 보고 골라."
          />
          <Step
            no="2"
            title="상세 확인"
            desc="설명/구성/구매 여부(purchased) 확인."
          />
          <Step
            no="3"
            title="로그인 & 시작"
            desc="소셜 로그인 후(필요시) 회원정보 작성 → 이용."
          />
        </ol>

        <div className="mt-6 flex flex-wrap gap-2">
          <Button onClick={() => navigate('/templates')}>지금 템플릿 보기</Button>
          <Button variant="ghost" onClick={() => navigate('/login')}>
            소셜 로그인
          </Button>
        </div>
      </section>

      <section className="rounded-3xl border border-zinc-200 bg-white p-8 dark:border-zinc-800 dark:bg-zinc-950">
        <h2 className="text-xl font-semibold">자주 묻는 질문</h2>

        <div className="mt-6 divide-y divide-zinc-200 dark:divide-zinc-800">
          <Faq
            q="무료 템플릿도 있나요?"
            a="있어. MVP에서는 무료/유료 구분이 잘 보이게 UI로 먼저 정돈 중."
          />
          <Faq
            q="인증은 어떻게 되나요?"
            a="소셜 로그인 기반이고, 프론트는 access token을 세션에 저장해서 요청 헤더에 자동으로 붙여."
          />
          <Faq
            q="지금 당장 제공되는 기능 범위는?"
            a="1차는 목록/상세/로그인/가이드 중심으로 ‘서비스 형태’ 완성. 구매/다운로드/리뷰는 2차."
          />
        </div>
      </section>

      <section className="flex flex-col items-start justify-between gap-3 rounded-3xl border border-zinc-200 bg-linear-to-b from-white to-zinc-50 p-8 dark:border-zinc-800 dark:from-zinc-950 dark:to-zinc-900 md:flex-row md:items-center">
        <div className="space-y-1">
          <h3 className="text-lg font-semibold">이제 화면이 “서비스 같다”로 가는 구간</h3>
          <p className="text-sm text-zinc-600 dark:text-zinc-300">
            다음은 템플릿 목록을 카드형으로 바꾸고, 상세 페이지 CTA까지 붙이면 MVP 완성감 난다.
          </p>
        </div>
        <div className="flex gap-2">
          <Button onClick={() => navigate('/templates')}>목록 UI 다듬기</Button>
        </div>
      </section>
    </div>
  )
}

function Card({ title, desc }: { title: string; desc: string }) {
  return (
    <div className="rounded-3xl border border-zinc-200 bg-white p-6 shadow-sm dark:border-zinc-800 dark:bg-zinc-950">
      <div className="text-base font-semibold">{title}</div>
      <div className="mt-2 text-sm text-zinc-600 dark:text-zinc-300">{desc}</div>
    </div>
  )
}

function Step({ no, title, desc }: { no: string; title: string; desc: string }) {
  return (
    <li className="rounded-2xl border border-zinc-200 p-5 dark:border-zinc-800">
      <div className="flex items-center gap-3">
        <span className="inline-flex h-7 w-7 items-center justify-center rounded-full bg-zinc-900 text-xs font-semibold text-white dark:bg-white dark:text-zinc-900">
          {no}
        </span>
        <div className="font-semibold">{title}</div>
      </div>
      <div className="mt-2 text-sm text-zinc-600 dark:text-zinc-300">{desc}</div>
    </li>
  )
}

function Faq({ q, a }: { q: string; a: string }) {
  return (
    <div className="py-4">
      <div className="font-medium">{q}</div>
      <div className="mt-1 text-sm text-zinc-600 dark:text-zinc-300">{a}</div>
    </div>
  )
}
