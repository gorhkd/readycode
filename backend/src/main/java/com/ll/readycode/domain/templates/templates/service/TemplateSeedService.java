package com.ll.readycode.domain.templates.templates.service;

import com.ll.readycode.api.templates.dto.request.TemplateSeedRequest;
import com.ll.readycode.domain.categories.entity.Category;
import com.ll.readycode.domain.categories.service.CategoryService;
import com.ll.readycode.domain.templates.files.entity.TemplateFile;
import com.ll.readycode.domain.templates.files.service.TemplateFileService;
import com.ll.readycode.domain.templates.templates.entity.Template;
import com.ll.readycode.domain.templates.templates.repository.TemplateRepository;
import com.ll.readycode.domain.users.userprofiles.entity.UserProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TemplateSeedService {

  private final CategoryService categoryService;
  private final TemplateFileService templateFileService;
  private final TemplateRepository templateRepository;

  @Transactional
  public Template createFromSeed(TemplateSeedRequest request, UserProfile seller) {
    Category category = categoryService.findCategoryById(request.categoryId());

    TemplateFile templateFile =
        templateFileService.createFromPath(request.filePath(), request.originalFilename());

    Template template =
        Template.builder()
            .title(request.title())
            .description(request.description())
            .image(request.image())
            .price(request.price())
            .category(category)
            .seller(seller)
            .build();

    template.setTemplateFile(templateFile);
    return templateRepository.save(template);
  }
}
