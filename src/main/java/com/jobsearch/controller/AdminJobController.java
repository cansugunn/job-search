package com.jobsearch.controller;

import com.jobsearch.common.enums.WorkingPreference;
import com.jobsearch.data.dto.job.request.AdminSearchJobsRequestDto;
import com.jobsearch.data.dto.job.request.CreateJobRequestDto;
import com.jobsearch.data.dto.job.request.UpdateJobRequestDto;
import com.jobsearch.data.dto.job.response.JobPostingResponseDto;
import com.jobsearch.service.JobService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/jobs")
@RequiredArgsConstructor
@Tag(name = "Admin – Job Management",
     description = "Create and update job postings")
@SecurityRequirement(name = "bearerAuth")
public class AdminJobController {

  private final JobService jobService;

  @Operation(summary = "List job postings for admin (no search history saved)")
  @PreAuthorize("hasRole('ADMIN') or hasRole('COMPANY')")
  @GetMapping
  public ResponseEntity<Page<JobPostingResponseDto>> listJobs(
      @RequestParam(required = false) String title,
      @RequestParam(required = false) WorkingPreference workingPreference,
      @RequestParam(required = false) Boolean active,
      @ParameterObject @PageableDefault(size = 10) Pageable pageable) {
    return ResponseEntity.ok(
        jobService.adminSearch(new AdminSearchJobsRequestDto(title, workingPreference, active), pageable));
  }

  @Operation(summary = "Create a new job posting")
  @PreAuthorize("hasRole('ADMIN') or hasRole('COMPANY')")
  @PostMapping
  public ResponseEntity<JobPostingResponseDto> createJob(@Valid @RequestBody CreateJobRequestDto dto) {
    return ResponseEntity.status(HttpStatus.CREATED)
                         .body(jobService.create(dto));
  }

  @Operation(summary = "Update an existing job posting")
  @PreAuthorize("hasRole('ADMIN') or hasRole('COMPANY')")
  @PutMapping("/{id}")
  public ResponseEntity<JobPostingResponseDto> updateJob(@PathVariable UUID id, @RequestBody UpdateJobRequestDto dto) {
    return ResponseEntity.ok(jobService.update(id, dto));
  }

  @Operation(summary = "Delete a job posting and its applications")
  @PreAuthorize("hasRole('ADMIN') or hasRole('COMPANY')")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteJob(@PathVariable UUID id) {
    jobService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
