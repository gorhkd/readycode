package com.ll.readycode.global.common.auth.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ll.readycode.global.exception.ErrorCode;
import com.ll.readycode.global.exception.ErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public void commence(
      HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException authException)
      throws IOException, ServletException {

    Object errorCodeAttr = request.getAttribute("errorCode");

    ErrorCode errorCode;

    if (errorCodeAttr instanceof ErrorCode) {
      errorCode = (ErrorCode) errorCodeAttr;

    } else {
      errorCode = ErrorCode.UNAUTHORIZED;
    }

    response.setStatus(errorCode.getStatus().value());
    response.setContentType("application/json;charset=UTF-8");

    ErrorResponse errorResponse = ErrorResponse.from(errorCode);
    String json = objectMapper.writeValueAsString(errorResponse);
    response.getWriter().write(json);
  }
}
