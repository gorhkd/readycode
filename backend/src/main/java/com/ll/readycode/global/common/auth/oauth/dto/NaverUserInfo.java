package com.ll.readycode.global.common.auth.oauth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class NaverUserInfo {

  private String resultcode;
  private String message;
  private Response response;

  @Getter
  public static class Response {
    private String id;
    private String email;
    private String name;
    private String nickname;

    @JsonProperty("profile_image")
    private String profileImage;

    private String gender;
    private String age;
    private String birthday;
  }

  // ✅ NaverOAuthService에서 쓰는 메서드
  public String getId() {
    return response != null ? response.getId() : null;
  }

  public String getEmail() {
    return response != null ? response.getEmail() : null;
  }
}

// 네이버 응답 구조
// {
//  "resultcode": "00",
//  "message": "success",
//  "response": {          // ← 이 안에 실제 데이터가 들어있음
//    "id": "123456",
//    "email": "test@naver.com",
//    "name": "홍길동"
//  }
// }
