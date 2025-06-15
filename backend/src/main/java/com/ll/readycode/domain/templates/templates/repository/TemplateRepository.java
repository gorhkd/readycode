package com.ll.readycode.domain.templates.templates.repository;

import com.ll.readycode.domain.templates.templates.entity.Template;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TemplateRepository
    extends JpaRepository<Template, Long>, TemplateRepositoryCustom {}
