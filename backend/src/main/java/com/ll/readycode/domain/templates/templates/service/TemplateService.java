package com.ll.readycode.domain.templates.templates.service;

import com.ll.readycode.api.templates.dto.request.TemplateCreateRequest;
import com.ll.readycode.api.templates.dto.request.TemplateUpdateRequest;
import com.ll.readycode.api.templates.dto.response.TemplateScrollResponse;
import com.ll.readycode.api.templates.dto.response.TemplateSummary;
import com.ll.readycode.domain.categories.entity.Category;
import com.ll.readycode.domain.categories.service.CategoryService;
import com.ll.readycode.domain.templates.templates.entity.Template;
import com.ll.readycode.domain.templates.templates.repository.TemplateRepository;
import com.ll.readycode.global.exception.CustomException;
import com.ll.readycode.global.exception.ErrorCode;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class TemplateService {
  private final TemplateRepository templateRepository;
  private final CategoryService categoryService;

  @Transactional
  public Template create(TemplateCreateRequest request) {
    Category category = categoryService.findCategoryById(request.categoryId());
    // TODO: 추후 인증 구현 후 SecurityContext에서 seller(UserProfile) 가져오기

    Template template =
        Template.builder()
            .title(request.title())
            .description(request.description())
            .image(request.image())
            .price(request.price())
            .category(category)
            .build();

    templateRepository.save(template);
    return template;
  }

  @Transactional
  public Template update(Long templatesId, @Valid TemplateUpdateRequest request) {
    Template template = findTemplateById(templatesId);
    // TODO: 추후 인증 후 유저 ID와 템플릿 판매자 ID와 비교 후 예외처리 로직 추가
    Category category = categoryService.findCategoryById(request.categoryId());

    template.update(
        request.title(), request.description(), request.price(), request.image(), category);
    return template;
  }

  @Transactional
  public void delete(Long templatesId) {
    Template template = findTemplateById(templatesId);

    // TODO: 현재 로그인한 유저의 ID와 템플릿의 판매자 ID 비교 후 권한 체크
    templateRepository.delete(template);
  }

  public TemplateScrollResponse getTemplates(LocalDateTime cursor, int limit) {
    List<Template> templates = templateRepository.findScrollTemplates(cursor, limit);

    LocalDateTime nextCursor =
        templates.isEmpty() ? null : templates.get(templates.size() - 1).getCreatedAt();

    List<TemplateSummary> result = templates.stream().map(TemplateSummary::from).toList();

    return new TemplateScrollResponse(result, nextCursor);
  }

  @Transactional(readOnly = true)
  public Template findTemplateById(Long templatesId) {
    return templateRepository
        .findById(templatesId)
        .orElseThrow(() -> new CustomException(ErrorCode.TEMPLATE_NOT_FOUND));
  }
}
