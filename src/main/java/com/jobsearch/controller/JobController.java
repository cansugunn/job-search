package com.jobsearch.controller;

import com.jobsearch.data.dto.job.request.SearchJobsRequestDto;
import com.jobsearch.data.dto.job.response.ApplyResponseDto;
import com.jobsearch.data.dto.job.response.HasAppliedResponseDto;
import com.jobsearch.data.dto.job.response.JobDetailResponseDto;
import com.jobsearch.data.dto.job.response.JobPostingResponseDto;
import com.jobsearch.service.JobService;
import com.jobsearch.service.SearchHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static java.util.Objects.nonNull;

@Slf4j
@RestController
@RequestMapping("/api/v1/jobs")
@RequiredArgsConstructor
@Tag(name = "Job Search",
     description = "Search and apply for job postings")
public class JobController {

  private final JobService jobService;

  private final SearchHistoryService searchHistoryService;

  @Operation(summary = "Search job postings with filters and pagination")
  @GetMapping
  public ResponseEntity<Page<JobPostingResponseDto>>
  search(@ParameterObject SearchJobsRequestDto request,
         @ParameterObject @PageableDefault(size = 10) Pageable pageable,
         @AuthenticationPrincipal Jwt jwt) {
    if (nonNull(jwt)) {
      try {
        searchHistoryService.saveSearch(jwt.getSubject(), request);
      } catch (Exception e) {
        log.warn("Search history save failed (non-fatal): {}", e.getMessage());
      }
    }
    return ResponseEntity.ok(jobService.search(request, pageable));
  }

  @Operation(summary = "Get job posting detail with related jobs")
  @GetMapping("/{id}")
  public ResponseEntity<JobDetailResponseDto> getById(@PathVariable UUID id) {
    return ResponseEntity.ok(jobService.getById(id));
  }

  @Operation(summary = "Get related job postings for a given job")
  @GetMapping("/{id}/related")
  public ResponseEntity<Page<JobPostingResponseDto>> getRelated(@PathVariable UUID id,
                                                                @ParameterObject @PageableDefault(size = 5)
                                                                Pageable pageable) {
    return ResponseEntity.ok(jobService.getRelated(id, pageable));
  }

  @Operation(summary = "Check whether the authenticated user has applied to this job")
  @GetMapping("/{id}/applied")
  public ResponseEntity<HasAppliedResponseDto> hasApplied(@PathVariable UUID id,
                                                          @AuthenticationPrincipal Jwt jwt) {
    boolean applied = jobService.hasApplied(id, jwt.getSubject());
    return ResponseEntity.ok(new HasAppliedResponseDto(applied));
  }

  @Operation(summary = "Apply to a job posting", security = @SecurityRequirement(name = "bearerAuth"))
  @PostMapping("/{id}/apply")
  public ResponseEntity<ApplyResponseDto>
  apply(@PathVariable UUID id, @AuthenticationPrincipal Jwt jwt) {
    return ResponseEntity.status(HttpStatus.CREATED)
                         .body(jobService.apply(id, jwt.getSubject()));
  }

  @Operation(summary = "Autocomplete job positions by keyword")
  @GetMapping("/autocomplete")
  public ResponseEntity<Page<String>>
  autocomplete(@RequestParam String query,
               @ParameterObject @PageableDefault(size = 10) Pageable pageable) {
    return ResponseEntity.ok(jobService.autocompletePositions(query, pageable));
  }

  @Operation(summary = "Get job postings by city")
  @GetMapping("/by-city")
  public ResponseEntity<Page<JobPostingResponseDto>>
  getByCity(@RequestParam String city,
            @ParameterObject @PageableDefault(size = 5) Pageable pageable) {
    return ResponseEntity.ok(jobService.getByCity(city, pageable));
  }
}
