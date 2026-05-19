package com.jobsearch.service;

import com.jobsearch.data.dto.job.request.SearchJobsRequestDto;
import com.jobsearch.data.dto.job.response.JobSearchResponseDto;
import com.jobsearch.data.dto.job.response.RecentSearchResponseDto;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SearchHistoryService {

  void saveSearch(@NotEmpty String userId,
                  @NotNull SearchJobsRequestDto request);

  Page<RecentSearchResponseDto> getRecentSearches(@NotEmpty String userId,
                                                  @NotNull Pageable pageable);

  Page<JobSearchResponseDto> getSearchesSince(@Positive int days,
                                              @NotNull Pageable pageable);
}
