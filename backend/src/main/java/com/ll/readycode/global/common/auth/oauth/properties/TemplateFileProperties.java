package com.ll.readycode.global.common.auth.oauth.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.unit.DataSize;

@Component
@ConfigurationProperties(prefix = "custom.template.file")
@Getter
@Setter
public class TemplateFileProperties {
  private String baseDir;
  private DataSize maxSize;
}
