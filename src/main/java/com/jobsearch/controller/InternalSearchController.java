package com.jobsearch.controller;

import com.jobsearch.data.dto.job.response.JobSearchResponseDto;
import com.jobsearch.service.SearchHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/searches")
@RequiredArgsConstructor
@Tag(name = "Internal – Search History",
     description = "Internal endpoints for notification service")
@SecurityRequirement(name = "bearerAuth")
public class InternalSearchController {

  private final SearchHistoryService searchHistoryService;

  @Operation(summary = "Get all job searches from the last N days")
  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/all")
  public ResponseEntity<Page<JobSearchResponseDto>> getAllSearchesSince(
      @RequestParam(defaultValue = "7") int days,
      @ParameterObject @PageableDefault(size = 50) Pageable pageable) {
    return ResponseEntity.ok(searchHistoryService.getSearchesSince(days, pageable));
  }
}
