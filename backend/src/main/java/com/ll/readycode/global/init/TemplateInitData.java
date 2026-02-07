package com.ll.readycode.global.init;

import com.ll.readycode.api.templates.dto.request.TemplateSeedRequest;
import com.ll.readycode.domain.categories.service.CategoryService;
import com.ll.readycode.domain.templates.templates.service.TemplateSeedService;
import com.ll.readycode.domain.users.userprofiles.entity.UserProfile;
import com.ll.readycode.domain.users.userprofiles.service.UserProfileService;
import java.nio.file.Path;
import java.nio.file.Paths;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.transaction.annotation.Transactional;

@Profile({"frontend", "local", "prod"})
@Configuration
@RequiredArgsConstructor
public class TemplateInitData {

  private final TemplateSeedService templateSeedService;
  private final UserProfileService userProfileService;
  private final CategoryService categoryService;

  @Autowired @Lazy TemplateInitData self;

  @Bean
  @Order(3)
  public ApplicationRunner templateApplicationRunner() {

    return args -> {
      self.createInitTemplates();
    };
  }

  @Transactional
  public void createInitTemplates() {
    UserProfile admin = userProfileService.findByNickname("관리자");

    Long catLoginId = categoryService.findByName("로그인").getId();
    Long catBoardId = categoryService.findByName("게시판").getId();

    Path dummyZip = Paths.get("seed/dummy.zip");

    for (int i = 0; i < 50; i++) {
      templateSeedService.createFromSeed(
          new TemplateSeedRequest(
              "스프링 로그인 템플릿",
              "JWT + OAuth2 기본 구성, 예외/필터 포함",
              "/images/templates/login.png",
              0,
              catLoginId,
              dummyZip,
              "login-template.zip"),
          admin);

      templateSeedService.createFromSeed(
          new TemplateSeedRequest(
              "게시판 CRUD 템플릿",
              "게시글/댓글, 페이지네이션, 검증/예외 포함",
              "/images/templates/board.png",
              100,
              catBoardId,
              dummyZip,
              "board-template.zip"),
          admin);

      templateSeedService.createFromSeed(
          new TemplateSeedRequest(
              "도메인 레이어 페키지 구조",
              "게시글/댓글, 페이지네이션, 검증/예외 포함",
              "/images/templates/board.png",
              100,
              catBoardId,
              dummyZip,
              "board-template.zip"),
          admin);
    }
  }
}
