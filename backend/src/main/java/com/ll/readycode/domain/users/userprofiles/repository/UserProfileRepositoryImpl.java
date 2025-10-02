package com.ll.readycode.domain.users.userprofiles.repository;

import com.ll.readycode.domain.users.userauths.entity.QUserAuth;
import com.ll.readycode.domain.users.userprofiles.entity.QUserProfile;
import com.ll.readycode.domain.users.userprofiles.entity.UserProfile;
import com.ll.readycode.domain.users.userprofiles.entity.UserRole;
import com.ll.readycode.global.common.types.OrderType;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class UserProfileRepositoryImpl implements UserProfileRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  public UserProfileRepositoryImpl(EntityManager em) {
    this.queryFactory = new JPAQueryFactory(em);
  }

  @Override
  public Optional<UserProfile> findProfileByProviderAndProviderId(
      String provider, String providerId) {

    QUserProfile user = QUserProfile.userProfile;
    QUserAuth auth = QUserAuth.userAuth;

    return Optional.ofNullable(
        queryFactory
            .selectFrom(user)
            .join(user.userAuths, auth)
            .where(auth.provider.eq(provider).and(auth.providerId.eq(providerId)))
            .fetchOne());
  }

  @Override
  public List<UserProfile> findAllByRoleWithCursor(
      UserRole role, int limit, Long cursor, String keyword, OrderType orderType) {

    QUserProfile user = QUserProfile.userProfile;

    return queryFactory
        .selectFrom(user)
        .where(buildSearchConditions(user, role, cursor, keyword, orderType))
        .orderBy((orderType == OrderType.DESC) ? user.id.desc() : user.id.asc())
        .limit(limit)
        .fetch();
  }

  private BooleanBuilder buildSearchConditions(
      QUserProfile user, UserRole role, Long cursor, String keyword, OrderType orderType) {

    BooleanBuilder builder = new BooleanBuilder();

    // 사용자 권한 조건 추가
    if (role != null) {
      builder.and(user.role.eq(role));
    }

    // 커서 조건 추가 (userId 기준)
    if (cursor != null) {
      if (orderType == OrderType.DESC) {
        builder.and(user.id.lt(cursor));
      } else {
        builder.and(user.id.gt(cursor));
      }
    }

    // 키워드 조건 추가 (nickname or social emails)
    if (keyword != null) {
      builder.and(
          user.nickname
              .containsIgnoreCase(keyword)
              .or(user.userAuths.any().email.containsIgnoreCase(keyword)));
    }

    return builder;
  }
}
