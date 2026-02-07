// 목록(커서 스크롤)에서 내려오는 아이템: TemplateSummary (백엔드)
export type TemplateSummary = {
  id: number
  title: string
  price: number
  category: string
  reviewCount: number
  avgRating: string
  createdAt: string
}

// 목록 응답: TemplateScrollResponse (백엔드)
export type TemplatesScrollData = {
  templates: TemplateSummary[]
  nextCursor: string | null
}

// 상세 응답: TemplateDetailResponse (백엔드)
export type TemplateDetail = {
  templateId: number
  title: string
  description: string
  imageUrl: string | null
  price: number
  category: string
  reviewCount: number
  avgRating: string
  createdAt: string
  updatedAt: string
  purchased: boolean
}
