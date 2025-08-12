package com.ll.readycode.domain.templates.templates.service;

import com.ll.readycode.api.templates.dto.request.TemplateCreateRequest;
import com.ll.readycode.api.templates.dto.request.TemplateUpdateRequest;
import com.ll.readycode.api.templates.dto.response.TemplateScrollResponse;
import com.ll.readycode.api.templates.dto.response.TemplateSummary;
import com.ll.readycode.domain.categories.entity.Category;
import com.ll.readycode.domain.categories.service.CategoryService;
import com.ll.readycode.domain.templates.templates.entity.Template;
import com.ll.readycode.domain.templates.templates.repository.TemplateRepository;
import com.ll.readycode.domain.users.userprofiles.entity.UserProfile;
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
  public Template create(TemplateCreateRequest request, UserProfile userProfile) {
    Category category = categoryService.findCategoryById(request.categoryId());

    Template template =
        Template.builder()
            .title(request.title())
            .description(request.description())
            .image(request.image())
            .price(request.price())
            .category(category)
            .seller(userProfile)
            .build();

    templateRepository.save(template);
    return template;
  }

  @Transactional
  public Template update(
      Long templatesId, @Valid TemplateUpdateRequest request, Long userProfileId) {
    Template template = findTemplateById(templatesId);
    Category category = categoryService.findCategoryById(request.categoryId());
    validateOwner(template, userProfileId);

    template.update(
        request.title(), request.description(), request.price(), request.image(), category);
    return template;
  }

  @Transactional
  public void delete(Long templatesId, Long userProfileId) {
    Template template = findTemplateById(templatesId);

    validateOwner(template, userProfileId);
    templateRepository.delete(template);
  }

  public TemplateScrollResponse getTemplateList(LocalDateTime cursor, int limit) {
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

  private void validateOwner(Template template, Long userProfileId) {
    if (!template.getSeller().getId().equals(userProfileId)) {
      throw new CustomException(ErrorCode.TEMPLATE_FORBIDDEN);
    }
  }
}
