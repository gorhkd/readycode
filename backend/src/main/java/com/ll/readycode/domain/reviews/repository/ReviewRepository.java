package com.ll.readycode.domain.reviews.repository;

import com.ll.readycode.domain.reviews.entity.Review;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long>, ReviewRepositoryCustom {
  boolean existsByUserProfileIdAndTemplateId(Long userId, Long templateId);

  Optional<Review> findByUserProfileIdAndTemplateId(Long userId, Long templateId);
}
