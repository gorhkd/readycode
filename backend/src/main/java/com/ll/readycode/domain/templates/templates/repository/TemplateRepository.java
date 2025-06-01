package com.ll.readycode.domain.templates.templates.repository;

import com.ll.readycode.domain.templates.templates.entity.Template;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TemplateRepository extends JpaRepository<Template, Long> {
  // 커서가 없을 때: 최신순 limit개
  @Query("SELECT t FROM Template t ORDER BY t.createdAt DESC")
  List<Template> findTopByOrderByCreatedAtDesc(Pageable pageable);

  // 커서가 있을 때: cursor 이전 데이터 중 최신순 limit개
  @Query("SELECT t FROM Template t WHERE t.createdAt < :cursor ORDER BY t.createdAt DESC")
  List<Template> findByCreatedAtBeforeOrderByCreatedAtDesc(
      @Param("cursor") LocalDateTime cursor, Pageable pageable);
}
