package com.ll.readycode.global.init;

import com.ll.readycode.domain.categories.entity.Category;
import com.ll.readycode.domain.categories.repository.CategoryRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("local")
@RequiredArgsConstructor
public class InitData {

  private final CategoryRepository categoryRepository;

  @PostConstruct
  public void init() {
    initCategories();
  }

  private void initCategories() {
    categoryRepository.save(Category.builder().name("로그인").build());
    categoryRepository.save(Category.builder().name("회원가입").build());
    categoryRepository.save(Category.builder().name("게시판").build());
    categoryRepository.save(Category.builder().name("파일 업로드").build());
    categoryRepository.save(Category.builder().name("결제").build());
  }
}
