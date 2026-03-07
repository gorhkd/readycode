
export function GuidePage() {

  return (
    <div className="space-y-10">
      <section className="rounded-3xl border border-zinc-200 bg-white p-8 shadow-sm dark:border-zinc-800 dark:bg-zinc-950">
        <div className="flex flex-col gap-4 md:flex-row md:items-center md:justify-between">
          <div className="space-y-3">
            <h1 className="text-3xl font-semibold tracking-tight">
              백엔드 템플릿 마켓
            </h1>
            <p className="max-w-2xl text-zinc-600 dark:text-zinc-300">
              인증, 게시판, 파일 업로드, 예외 처리 등 반복되는 백엔드 기능을 템플릿으로 제공합니다.
            </p>
          </div>
        </div>

        <div className="mt-6 flex flex-wrap gap-3">
          {[
            'Spring Boot',
            '구조화된 폴더 구성',
            '에러/응답 포맷 통일',
            '확장 가능한 설계',
            '가독성 있는 코드',
          ].map((t) => (
            <span
              key={t}
              className="rounded-full border border-zinc-200 px-4 py-1.5 text-sm font-medium text-zinc-700 dark:border-zinc-800 dark:text-zinc-200"
            >
              {t}
            </span>
          ))}
        </div>
      </section>

      <section className="grid gap-4 md:grid-cols-3">
        <Card
          title="반복 작업 단축"
          desc="인증, CRUD, 예외 처리 구성을 빠르게 시작할 수 있습니다."
        />
        <Card
          title="정리된 구조"
          desc="계층과 패키지 구조가 정리된 상태로 시작할 수 있습니다."
        />
        <Card
          title="확장 고려"
          desc="다운로드, 리뷰, 관리자 기능 확장을 고려한 구조입니다."
        />
      </section>

      <section className="rounded-3xl border border-zinc-200 bg-white p-8 dark:border-zinc-800 dark:bg-zinc-950">
        <h2 className="text-xl font-semibold">사용 흐름</h2>
        <p className="mt-2 text-sm text-zinc-600 dark:text-zinc-300">
          기본 이용 절차를 간단히 확인할 수 있습니다.
        </p>

        <ol className="mt-6 grid gap-4 md:grid-cols-3">
          <Step
            no="1"
            title="템플릿 확인"
            desc="필요한 범위에 맞는 백엔드 코드를 확인합니다."
          />
          <Step
            no="2"
            title="로그인"
            desc="로그인 후 템플릿을 다운로드 받을 수 있습니다."
          />
          <Step
            no="3"
            title="다운로드"
            desc="상세 확인 후 템플릿을 다운로드합니다."
          />
        </ol>
      </section>

      <section className="rounded-3xl border border-zinc-200 bg-white p-8 dark:border-zinc-800 dark:bg-zinc-950">
        <h2 className="text-xl font-semibold">자주 묻는 질문</h2>

        <div className="mt-6 divide-y divide-zinc-200 dark:divide-zinc-800">
          <Faq
            q="무료 템플릿도 있나요?"
            a="무료와 유료 템플릿을 함께 제공할 수 있도록 구성되어 있습니다."
          />
          <Faq
            q="인증은 어떻게 진행되나요?"
            a="소셜 로그인 기반으로 인증이 진행됩니다."
          />
          <Faq
            q="현재 제공 범위는 어디까지인가요?"
            a="현재는 인증/인가 코드 템플릿만 구성되어 있습니다."
          />
        </div>
      </section>
    </div>
  )
}

function Card({ title, desc }: { title: string; desc: string }) {
  return (
    <div className="rounded-3xl border border-zinc-200 bg-white p-6 shadow-sm dark:border-zinc-800 dark:bg-zinc-950">
      <div className="text-base font-semibold">{title}</div>
      <div className="mt-2 text-sm leading-relaxed text-zinc-600 dark:text-zinc-300">
        {desc}
      </div>
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