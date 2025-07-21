package com.ll.readycode.domain.users.userprofiles.repository;

import com.ll.readycode.domain.users.userauths.entity.QUserAuth;
import com.ll.readycode.domain.users.userprofiles.entity.QUserProfile;
import com.ll.readycode.domain.users.userprofiles.entity.UserProfile;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.Optional;

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
}
