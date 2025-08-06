import { createFileRoute } from '@tanstack/react-router'

export const Route = createFileRoute('/guide')({
  component: Guide,
})

function Guide() {
  const content = [{
    title: '소개',
    subTitles: ['내용', '내용']
  }, {
    title: '가이드라인',
    subTitles: ['이용 흐름 안내', '템플릿 사용법', '템플릿 유형 설명', ' 다운로드/구매 방식', '리뷰 & 신고 규칙', '템플릿 제작가 가이드 (선택)']
  }]

  return (
    <div className="flex gap-8 justify-between">
      <aside className="flex flex-col gap-6 w-80 bg-base-200 p-2 pt-4 rounded-lg">
        {content.map((item, i) => (
          <div key={i}>
            <h2 className="text-base-content/70 ml-2">{item.title}</h2>
            <ul className="list">
              {item.subTitles.map((subTitle, iSubTitle) => (
                <a href="#">
                  <li key={iSubTitle} className="list-row hover:bg-base-100 rounded-none">
                    {iSubTitle + 1}. {subTitle}
                  </li>
                </a>
              ))}
            </ul>
          </div>
        ))}
      </aside>
      <div className="border min-w-200 w-full">내용</div>

    </div>
  )
}
