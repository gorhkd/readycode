package com.ll.readycode.global.common.auth.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ll.readycode.global.exception.ErrorCode;
import com.ll.readycode.global.exception.ErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public void handle(
      HttpServletRequest request,
      HttpServletResponse response,
      AccessDeniedException accessDeniedException)
      throws IOException, ServletException {

    ErrorCode errorCode = ErrorCode.FORBIDDEN;

    response.setStatus(errorCode.getStatus().value());
    response.setContentType("application/json;charset=UTF-8");

    ErrorResponse errorResponse = ErrorResponse.from(errorCode);
    String json = objectMapper.writeValueAsString(errorResponse);
    response.getWriter().write(json);
  }
}
