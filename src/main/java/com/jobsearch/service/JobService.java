package com.jobsearch.service;

import com.jobsearch.data.dto.job.request.AdminSearchJobsRequestDto;
import com.jobsearch.data.dto.job.request.CreateJobRequestDto;
import com.jobsearch.data.dto.job.request.SearchJobsRequestDto;
import com.jobsearch.data.dto.job.request.UpdateJobRequestDto;
import com.jobsearch.data.dto.job.response.ApplyResponseDto;
import com.jobsearch.data.dto.job.response.JobDetailResponseDto;
import com.jobsearch.data.dto.job.response.JobPostingResponseDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface JobService {

  JobPostingResponseDto create(@NotNull @Valid CreateJobRequestDto dto);

  JobPostingResponseDto update(@NotNull UUID id,
                               @NotNull @Valid UpdateJobRequestDto dto);

  Page<JobPostingResponseDto> search(@NotNull SearchJobsRequestDto request,
                                     @NotNull Pageable pageable);

  JobDetailResponseDto getById(@NotNull UUID id);

  Page<JobPostingResponseDto> getRelated(@NotNull UUID id,
                                         @NotNull Pageable pageable);

  ApplyResponseDto apply(@NotNull UUID jobId,
                         @NotEmpty String userId);

  boolean hasApplied(@NotNull UUID jobId,
                     @NotEmpty String userId);

  Page<JobPostingResponseDto> adminSearch(@NotNull AdminSearchJobsRequestDto request,
                                          @NotNull Pageable pageable);

  void delete(@NotNull UUID id);

  Page<JobPostingResponseDto> getByCity(@NotEmpty String city,
                                        @NotNull Pageable pageable);

  Page<String> autocompletePositions(@NotEmpty String query,
                                     @NotNull Pageable pageable);
}
