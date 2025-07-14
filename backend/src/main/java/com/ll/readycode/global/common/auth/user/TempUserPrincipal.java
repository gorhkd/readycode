package com.ll.readycode.global.common.auth.user;

import lombok.Builder;

@Builder
public record TempUserPrincipal(String provider, String providerId, String email) {}
