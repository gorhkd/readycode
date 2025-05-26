package com.ll.readycode.domain.templates.templates.service;

import com.ll.readycode.api.templates.dto.request.TemplateCreateRequest;
import com.ll.readycode.domain.categories.entity.Category;
import com.ll.readycode.domain.categories.service.CategoryService;
import com.ll.readycode.domain.templates.templates.entity.Template;
import com.ll.readycode.domain.templates.templates.repository.TemplateRepository;
import com.ll.readycode.domain.users.userprofiles.entity.UserProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TemplateService {
  private final TemplateRepository templateRepository;
  private final CategoryService categoryService;

  public Template create(TemplateCreateRequest request) {

    Category category = categoryService.findCategoryById(request.categoryId());
    // TODO: 추후 인증 구현 후 SecurityContext에서 seller(UserProfile) 가져오기
    UserProfile userProfile = new UserProfile(); // 현재는 비어있는 객체로 임시 생성

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
}
