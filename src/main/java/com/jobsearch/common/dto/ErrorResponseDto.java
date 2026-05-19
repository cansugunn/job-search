package com.jobsearch.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import java.util.Map;

import static java.time.LocalDateTime.now;
import static java.util.Collections.emptyMap;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponseDto(int status,
                               String message,
                               LocalDateTime timestamp,
                               Map<String, String> fieldErrors) {

  private static final String VALIDATION_FAILED = "Validation failed";

  public static ErrorResponseDto of(int status, String message) {
    return new ErrorResponseDto(status, message, now(), emptyMap());
  }

  public static ErrorResponseDto withFieldErrors(int status, Map<String, String> fieldErrors) {
    return new ErrorResponseDto(status, VALIDATION_FAILED, now(), fieldErrors);
  }
}
