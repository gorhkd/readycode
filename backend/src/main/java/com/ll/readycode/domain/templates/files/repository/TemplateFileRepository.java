package com.ll.readycode.domain.templates.files.repository;

import com.ll.readycode.domain.templates.files.entity.TemplateFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TemplateFileRepository extends JpaRepository<TemplateFile, Long> {}
