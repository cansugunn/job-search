package com.jobsearch.common.exceptionhandler;

import com.jobsearch.common.dto.ErrorResponseDto;
import com.jobsearch.common.exception.BusinessException;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.jobsearch.common.dto.ErrorResponseDto.of;
import static com.jobsearch.common.dto.ErrorResponseDto.withFieldErrors;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler
  public ResponseEntity<ErrorResponseDto>
  handleValidation(MethodArgumentNotValidException methodArgumentNotValidException) {
    Map<String, String> fieldErrors = new LinkedHashMap<>();
    methodArgumentNotValidException.getBindingResult()
                                   .getFieldErrors()
                                   .forEach(e -> fieldErrors.put(e.getField(), e.getDefaultMessage()));
    return ResponseEntity.badRequest()
                         .body(withFieldErrors(HttpStatus.BAD_REQUEST.value(), fieldErrors));
  }

  @ExceptionHandler
  public ResponseEntity<ErrorResponseDto> handleBusiness(BusinessException ex) {
    return ResponseEntity.badRequest()
                         .body(of(HttpStatus.BAD_REQUEST.value(), ex.getMessage()));
  }

  @ExceptionHandler
  public ResponseEntity<ErrorResponseDto> handleDataIntegrity(DataIntegrityViolationException ex) {
    return ResponseEntity.status(HttpStatus.CONFLICT)
                         .body(of(HttpStatus.CONFLICT.value(), ex.getMessage()));
  }

  @ExceptionHandler
  public ResponseEntity<ErrorResponseDto> handleAccessDenied(AccessDeniedException ex) {
    return ResponseEntity.status(HttpStatus.FORBIDDEN)
                         .body(of(HttpStatus.FORBIDDEN.value(), ex.getMessage()));
  }

  @ExceptionHandler
  public ResponseEntity<ErrorResponseDto> handleAuthentication(AuthenticationException ex) {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                         .body(of(HttpStatus.UNAUTHORIZED.value(), ex.getMessage()));
  }

  @ExceptionHandler
  public ResponseEntity<ErrorResponseDto> handleGeneric(Exception ex) {
    return ResponseEntity.internalServerError()
                         .body(of(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage()));
  }
}
