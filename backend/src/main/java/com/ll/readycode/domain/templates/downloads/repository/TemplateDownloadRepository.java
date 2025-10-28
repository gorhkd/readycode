package com.ll.readycode.domain.templates.downloads.repository;

import com.ll.readycode.domain.templates.downloads.entity.TemplateDownload;
import com.ll.readycode.domain.users.userprofiles.entity.UserProfile;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TemplateDownloadRepository
    extends JpaRepository<TemplateDownload, Long>, TemplateDownloadCustom {

  List<TemplateDownload> findByUser(UserProfile user);
}
