package com.jobsearch.service;

import com.jobsearch.data.dto.job.response.JobDetailResponseDto;
import jakarta.validation.constraints.NotNull;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;

@Validated
public abstract class JobCacheService {

  protected static final String JOB_CACHE_PREFIX = "job:";

  @Value("${job-search.cache.job-posting.ttl:600}")
  protected long cacheTtlSeconds;

  protected String toKey(@NotNull UUID id) {
    return JOB_CACHE_PREFIX + id;
  }

  public abstract Optional<JobDetailResponseDto> find(@NotNull UUID id);

  public abstract void save(@NotNull JobDetailResponseDto jobDetailResponseDto);

  public abstract void evict(@NotNull UUID id);
}
