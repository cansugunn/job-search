package com.jobsearch.service.impl;

import com.jobsearch.data.dto.job.response.JobDetailResponseDto;
import com.jobsearch.service.JobCacheService;
import java.time.Duration;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import static java.util.Objects.nonNull;

@Slf4j
@Service
@RequiredArgsConstructor
public class JobRedisCacheServiceImpl extends JobCacheService {

  private final RedisTemplate<String, JobDetailResponseDto> redisTemplate;

  @Override
  public Optional<JobDetailResponseDto> find(UUID id) {
    String cacheKey = toKey(id);
    try {
      JobDetailResponseDto jobDetailResponseDto = redisTemplate.opsForValue().get(cacheKey);
      if (nonNull(jobDetailResponseDto)) {
        log.info("Cache hit for job posting id={}", id);
        return Optional.of(jobDetailResponseDto);
      }
    } catch (Exception exception) {
      log.warn("Redis cache get failed for key={}: {}", cacheKey, exception.getMessage());
    }

    return Optional.empty();
  }

  @Override
  public void save(JobDetailResponseDto jobDetailResponseDto) {
    UUID id = jobDetailResponseDto.posting().id();
    String cacheKey = toKey(id);
    try {
      redisTemplate.opsForValue()
                   .set(cacheKey, jobDetailResponseDto, Duration.ofSeconds(cacheTtlSeconds));
      log.info("Cache miss – stored job posting id={}", id);
    } catch (Exception exception) {
      log.warn("Redis cache set failed for key={}: {}", cacheKey, exception.getMessage());
    }
  }

  @Override
  public void evict(UUID id) {
    redisTemplate.delete(JOB_CACHE_PREFIX + id);
  }
}
