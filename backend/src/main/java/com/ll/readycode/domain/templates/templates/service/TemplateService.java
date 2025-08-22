package com.ll.readycode.domain.templates.templates.service;

import com.ll.readycode.api.templates.dto.request.TemplateCreateRequest;
import com.ll.readycode.api.templates.dto.request.TemplateUpdateRequest;
import com.ll.readycode.api.templates.dto.response.TemplateScrollResponse;
import com.ll.readycode.api.templates.dto.response.TemplateSummary;
import com.ll.readycode.domain.categories.entity.Category;
import com.ll.readycode.domain.categories.service.CategoryService;
import com.ll.readycode.domain.templates.files.entity.TemplateFile;
import com.ll.readycode.domain.templates.files.service.TemplateFileService;
import com.ll.readycode.domain.templates.query.TemplateSortType;
import com.ll.readycode.domain.templates.templates.entity.Template;
import com.ll.readycode.domain.templates.templates.repository.TemplateRepository;
import com.ll.readycode.domain.users.userprofiles.entity.UserProfile;
import com.ll.readycode.global.common.pagination.PaginationPolicy;
import com.ll.readycode.global.common.types.OrderType;
import com.ll.readycode.global.exception.CustomException;
import com.ll.readycode.global.exception.ErrorCode;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class TemplateService {
  private final TemplateRepository templateRepository;
  private final TemplateFileService templateFileService;
  private final CategoryService categoryService;

  private static final DateTimeFormatter CURSOR_FMT = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
  private static final String DELIM = "|";

  @Transactional
  public Template create(
      TemplateCreateRequest request, MultipartFile file, UserProfile userProfile) {
    Category category = categoryService.findCategoryById(request.categoryId());
    TemplateFile templateFile = templateFileService.create(file);

    Template template =
        Template.builder()
            .title(request.title())
            .description(request.description())
            .image(request.image())
            .price(request.price())
            .category(category)
            .seller(userProfile)
            .build();

    template.setTemplateFile(templateFile);

    templateRepository.save(template);
    return template;
  }

  @Transactional
  public Template update(
      Long templatesId,
      @Valid TemplateUpdateRequest request,
      UserProfile userProfile,
      MultipartFile file) {
    Template template = findTemplateById(templatesId);
    validateTemplateOwner(template, userProfile.getId());

    Category category = categoryService.findCategoryById(request.categoryId());

    template.update(
        request.title(), request.description(), request.price(), request.image(), category);

    if (file != null && !file.isEmpty()) {
      TemplateFile templateFile = templateFileService.updateFile(template.getTemplateFile(), file);
      template.setTemplateFile(templateFile);
    }

    return template;
  }

  @Transactional
  public void delete(Long templatesId, UserProfile userProfile) {
    Template template = findTemplateById(templatesId);

    validateTemplateOwner(template, userProfile.getId());
    templateFileService.deleteFile(template.getTemplateFile());

    templateRepository.delete(template);
  }

  @Transactional(readOnly = true)
  public TemplateScrollResponse getTemplateList(
      String cursor, String sort, String order, Long categoryId, Integer limit) {
    TemplateSortType sortType = TemplateSortType.from(sort);
    OrderType orderType = OrderType.from(order);

    if (categoryId != null) {
      categoryService.assertCategoryExists(categoryId);
    }

    int pageSize = PaginationPolicy.clamp(limit);
    int fetchSize = pageSize + 1;

    List<Template> templates =
        templateRepository.findScrollTemplates(cursor, sortType, orderType, categoryId, fetchSize);

    boolean hasNext = templates.size() > pageSize;
    if (hasNext) {
      templates = templates.subList(0, pageSize);
    }

    String nextCursor = null;
    if (hasNext && !templates.isEmpty()) {
      Template last = templates.get(templates.size() - 1);
      nextCursor = encodeCursor(last, sortType);
    }

    List<TemplateSummary> result = templates.stream().map(TemplateSummary::from).toList();

    return new TemplateScrollResponse(result, nextCursor);
  }

  private String encodeCursor(Template t, TemplateSortType sortType) {
    String ts = t.getCreatedAt().format(CURSOR_FMT);
    Long id = t.getId();

    return switch (sortType) {
      case LATEST -> ts + DELIM + id;
      case RATING -> {
        BigDecimal rating = t.getAvgRating() == null ? BigDecimal.ZERO : t.getAvgRating();
        String r = rating.stripTrailingZeros().toPlainString();
        yield r + DELIM + ts + DELIM + id;
      }
      case POPULAR -> {
        long cnt = (t.getPurchaseCount() == null) ? 0L : t.getPurchaseCount();
        yield cnt + DELIM + ts + DELIM + id;
      }
    };
  }

  @Transactional(readOnly = true)
  public Template findTemplateById(Long templatesId) {
    return templateRepository
        .findById(templatesId)
        .orElseThrow(() -> new CustomException(ErrorCode.TEMPLATE_NOT_FOUND));
  }

  private void validateTemplateOwner(Template template, Long userId) {
    if (!template.getSeller().getId().equals(userId)) {
      throw new CustomException(ErrorCode.TEMPLATE_ACCESS_DENIED);
    }
  }
}
