package com.ll.readycode.global.init;

import com.ll.readycode.api.categories.dto.request.CategoryRequest;
import com.ll.readycode.domain.categories.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.transaction.annotation.Transactional;

@Profile({"frontend", "local"})
@Configuration
@RequiredArgsConstructor
public class CategoryInitData {

  private final CategoryService categoryService;

  @Autowired @Lazy CategoryInitData self;

  @Bean
  @Order(2)
  public ApplicationRunner categoryApplicationRunner() {
    return args -> {
      self.createInitCategories();
    };
  }

  @Transactional
  public void createInitCategories() {

    if (categoryService.countCategories() > 0) {
      return;
    }

    categoryService.createCategory(new CategoryRequest("패키지구조"));
    categoryService.createCategory(new CategoryRequest("로그인"));
    categoryService.createCategory(new CategoryRequest("회원가입"));
    categoryService.createCategory(new CategoryRequest("게시판"));
    categoryService.createCategory(new CategoryRequest("파일 업로드"));
    categoryService.createCategory(new CategoryRequest("결제"));
  }
}
