package com.ll.readycode.global.common.entity;

import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import lombok.Getter;
import org.springframework.data.annotation.LastModifiedDate;

@Getter
@MappedSuperclass
public abstract class BaseEntity extends BaseCreatedOnlyEntity {

  @LastModifiedDate protected LocalDateTime updatedAt;
}
