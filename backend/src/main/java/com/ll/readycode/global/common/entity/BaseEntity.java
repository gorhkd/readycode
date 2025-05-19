package com.ll.readycode.global.common.entity;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
public abstract class BaseEntity extends BaseCreatedOnlyEntity {

    @LastModifiedDate
    protected LocalDateTime updatedAt;
}