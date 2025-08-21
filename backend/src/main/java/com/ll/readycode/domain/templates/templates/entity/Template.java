package com.ll.readycode.domain.templates.templates.entity;

import static com.ll.readycode.global.common.util.NumberFormatUtils.toScaledRating;

import com.ll.readycode.domain.categories.entity.Category;
import com.ll.readycode.domain.templates.files.entity.TemplateFile;
import com.ll.readycode.domain.users.userprofiles.entity.UserProfile;
import com.ll.readycode.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
public class Template extends BaseEntity {

  @Column(nullable = false)
  private String title;

  @Column(columnDefinition = "TEXT")
  private String description;

  @Column(nullable = false)
  private int price;

  @Column(nullable = false)
  private String image;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "category_id", nullable = false)
  private Category category;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "seller_id", nullable = false)
  private UserProfile seller;

  @OneToOne(
      mappedBy = "template",
      cascade = CascadeType.ALL,
      orphanRemoval = true,
      fetch = FetchType.LAZY)
  private TemplateFile templateFile;

  @Column(nullable = false)
  private long reviewCount = 0L;

  @Column(nullable = false)
  private long ratingSum = 0L; // 별점×10 누적합

  @Column(nullable = false, precision = 3, scale = 2)
  private BigDecimal avgRating = new BigDecimal("0.00"); // 표시/정렬용 0.00~5.00

  @Version private Long version;

  public void update(String title, String description, int price, String image, Category category) {
    this.title = title;
    this.description = description;
    this.price = price;
    this.image = image;
    this.category = category;
  }

  public void setTemplateFile(TemplateFile templateFile) {
    this.templateFile = templateFile;
    templateFile.setTemplate(this);
  }

  public void addReview(BigDecimal rating) {
    this.reviewCount++;
    this.ratingSum += toScaledRating(rating);
    recalcAvg();
  }

  public void updateReview(BigDecimal oldRating, BigDecimal newRating) {
    this.ratingSum += (toScaledRating(newRating) - toScaledRating(oldRating));
    this.recalcAvg();
  }

  public void removeReview(BigDecimal rating) {
    this.reviewCount = Math.max(0, this.reviewCount - 1);
    this.ratingSum = Math.max(0, this.ratingSum - toScaledRating(rating));
    this.recalcAvg();
  }

  public void recalcAvg() {
    if (reviewCount == 0) {
      this.avgRating = new BigDecimal("0.00");
      ratingSum = 0;
      return;
    }
    // (ratingSum / 10.0) / reviewCount  → 소수 2자리 반올림
    BigDecimal sum = BigDecimal.valueOf(ratingSum).divide(BigDecimal.TEN);
    this.avgRating = sum.divide(BigDecimal.valueOf(reviewCount), 2, RoundingMode.HALF_UP);
  }
}
