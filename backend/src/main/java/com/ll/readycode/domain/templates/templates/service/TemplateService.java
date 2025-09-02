package com.ll.readycode.domain.templates.templates.service;

import static java.nio.charset.StandardCharsets.UTF_8;

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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
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

  private static final Base64.Encoder B64 = Base64.getUrlEncoder().withoutPadding();
  private static final Base64.Decoder B64D = Base64.getUrlDecoder();
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

    LocalDateTime ts = null;
    Long lastId = null;
    BigDecimal rating = null;
    Long cnt = null;

    if (cursor != null && !cursor.isBlank()) {
      switch (sortType) {
        case LATEST -> {
          String[] p = decodeCursorParts(cursor, 2); // [ts, id]
          ts = LocalDateTime.parse(p[0], CURSOR_FMT);
          lastId = parseLongStrict(p[1]);
        }
        case RATING -> {
          String[] p = decodeCursorParts(cursor, 3); // [rating, ts, id]
          rating = new BigDecimal(p[0]);
          ts = LocalDateTime.parse(p[1], CURSOR_FMT);
          lastId = parseLongStrict(p[2]);
        }
        case POPULAR -> {
          String[] p = decodeCursorParts(cursor, 3); // [count, ts, id]
          cnt = parseLongStrict(p[0]);
          ts = LocalDateTime.parse(p[1], CURSOR_FMT);
          lastId = parseLongStrict(p[2]);
        }
      }
    }

    List<Template> templates =
        templateRepository.findScrollTemplates(
            sortType, orderType, categoryId, fetchSize, ts, lastId, rating, cnt);

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

    String raw =
        switch (sortType) {
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

    return B64.encodeToString(raw.getBytes(UTF_8));
  }

  private String[] decodeCursorParts(String cursor, int expectedParts) {
    String trimmed = cursor == null ? "" : cursor.trim();
    String raw = new String(B64D.decode(trimmed), UTF_8);
    String[] parts = raw.split("\\|", -1);
    if (parts.length != expectedParts) throw new CustomException(ErrorCode.INVALID_INPUT_VALUE);
    return parts;
  }

  private long parseLongStrict(String s) {
    try {
      return Long.parseLong(s);
    } catch (NumberFormatException e) {
      throw new CustomException(ErrorCode.INVALID_INPUT_VALUE);
    }
  }

  @Transactional(readOnly = true)
  public Template findTemplateWithCategoryById(Long templateId) {
    return templateRepository
        .findByIdWithCategory(templateId)
        .orElseThrow(() -> new CustomException(ErrorCode.TEMPLATE_NOT_FOUND));
  }

  @Transactional(readOnly = true)
  public Template findTemplateById(Long templatesId) {
    return templateRepository
        .findById(templatesId)
        .orElseThrow(() -> new CustomException(ErrorCode.TEMPLATE_NOT_FOUND));
  }

  @Transactional
  public void incrementPurchaseCount(Long templateId) {
    templateRepository.incrementPurchaseCount(templateId);
  }

  private void validateTemplateOwner(Template template, Long userId) {
    if (!template.getSeller().getId().equals(userId)) {
      throw new CustomException(ErrorCode.TEMPLATE_ACCESS_DENIED);
    }
  }

  @Transactional(readOnly = true)
  public Long countTemplates() {
    return templateRepository.count();
  }
}
