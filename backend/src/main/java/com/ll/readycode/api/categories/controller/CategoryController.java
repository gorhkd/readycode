package com.ll.readycode.api.categories.controller;

import com.ll.readycode.domain.categories.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class CategoryController {
  private final CategoryService categoryService;
}
