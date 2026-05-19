package com.jobsearch.service.impl;

import com.jobsearch.common.exception.BusinessException;
import com.jobsearch.data.dto.job.request.AdminSearchJobsRequestDto;
import com.jobsearch.data.dto.job.request.CreateJobRequestDto;
import com.jobsearch.data.dto.job.request.SearchJobsRequestDto;
import com.jobsearch.data.dto.job.request.UpdateJobRequestDto;
import com.jobsearch.data.dto.job.response.ApplyResponseDto;
import com.jobsearch.data.dto.job.response.JobDetailResponseDto;
import com.jobsearch.data.dto.job.response.JobPostingResponseDto;
import com.jobsearch.data.entity.Application;
import com.jobsearch.data.entity.Company;
import com.jobsearch.data.entity.JobPosting;
import com.jobsearch.data.entity.Town;
import com.jobsearch.data.mapper.JobMapper;
import com.jobsearch.data.repository.ApplicationRepository;
import com.jobsearch.data.repository.CompanyRepository;
import com.jobsearch.data.repository.JobPostingRepository;
import com.jobsearch.data.repository.TownRepository;
import com.jobsearch.messaging.JobPostingProducer;
import com.jobsearch.service.JobCacheService;
import com.jobsearch.service.JobService;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import static com.jobsearch.data.repository.specification.JobPostingSpecification.byCityName;
import static com.jobsearch.data.repository.specification.JobPostingSpecification.fromAdminRequest;
import static com.jobsearch.data.repository.specification.JobPostingSpecification.fromRequest;
import static java.util.Collections.emptyList;
import static java.util.Objects.nonNull;
import static org.springframework.data.domain.Page.empty;

@Validated
@Slf4j
@Service
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {

  private final JobPostingRepository jobPostingRepository;

  private final ApplicationRepository applicationRepository;

  private final TownRepository townRepository;

  private final CompanyRepository companyRepository;

  private final JobMapper jobMapper;

  private final JobPostingProducer jobPostingProducer;

  private final JobCacheService jobCacheService;

  @Override
  @Transactional
  public JobPostingResponseDto create(CreateJobRequestDto dto) {
    Town town = townRepository.findById(dto.townId())
                              .orElseThrow(() -> new BusinessException("Town not found"));
    Company company = companyRepository.findById(dto.companyId())
                                       .orElseThrow(() -> new BusinessException("Company not found"));

    JobPosting jobPosting = jobMapper.toJobPosting(dto);
    jobPosting.setTown(town);
    jobPosting.setCompany(company);

    jobPostingRepository.save(jobPosting);
    jobPostingProducer.publishNewJobPosting(jobMapper.toNewJobPostingEvent(jobPosting));
    return jobMapper.toJobPostingResponseDto(jobPosting);
  }

  @Override
  @Transactional
  public JobPostingResponseDto update(UUID id, UpdateJobRequestDto dto) {
    JobPosting jobPosting = jobPostingRepository.findById(id)
                                                .orElseThrow(() -> new BusinessException("Job posting not found"));

    jobMapper.updateEntity(dto, jobPosting);
    if (nonNull(dto.townId())) {
      jobPosting.setTown(townRepository.findById(dto.townId())
                                       .orElseThrow(() -> new BusinessException("Town not found")));
    }

    jobPostingRepository.save(jobPosting);
    jobCacheService.evict(id);
    return jobMapper.toJobPostingResponseDto(jobPosting);
  }

  @Override
  public Page<JobPostingResponseDto> search(SearchJobsRequestDto request, Pageable pageable) {
    return jobPostingRepository.findAll(fromRequest(request), pageable)
                               .map(jobMapper::toJobPostingResponseDto);
  }

  @Override
  @Transactional(readOnly = true)
  public JobDetailResponseDto getById(UUID id) {
    Optional<JobDetailResponseDto> cacheResponseOptional = jobCacheService.find(id);
    if (cacheResponseOptional.isPresent()) {
      return cacheResponseOptional.get();
    }

    JobPosting jobPosting = jobPostingRepository.findById(id)
                                                .orElseThrow(() -> new BusinessException("Job posting not found"));
    List<JobPostingResponseDto> relatedJobPostingResponseDtoList = getRelatedForPosting(jobPosting);

    JobDetailResponseDto jobDetailResponseDto =
        jobMapper.toJobDetailResponseDto(jobMapper.toJobPostingResponseDto(jobPosting),
                                         relatedJobPostingResponseDtoList);
    jobCacheService.save(jobDetailResponseDto);
    return jobDetailResponseDto;
  }

  private List<JobPostingResponseDto> getRelatedForPosting(JobPosting jobPosting) {
    Optional<String> keywordOptional = jobPosting.getKeyword();
    if (keywordOptional.isEmpty()) {
      return emptyList();
    }

    return jobPostingRepository
        .findRelated(jobPosting.getId(),
                     jobPosting.getTown().getCity().getId(),
                     keywordOptional.get(),
                     PageRequest.of(0, 3))
        .stream()
        .map(jobMapper::toJobPostingResponseDto)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public Page<JobPostingResponseDto> getRelated(UUID id, Pageable pageable) {
    JobPosting jobPosting = jobPostingRepository.findById(id)
                                                .orElseThrow(() -> new BusinessException("Job posting not found"));
    Optional<String> keywordOptional = jobPosting.getKeyword();
    if (keywordOptional.isEmpty()) {
      return empty();
    }
    return jobPostingRepository
        .findRelatedPage(jobPosting.getId(),
                         jobPosting.getTown().getCity().getId(),
                         keywordOptional.get(),
                         pageable)
        .map(jobMapper::toJobPostingResponseDto);
  }

  @Override
  @Transactional
  public ApplyResponseDto apply(UUID jobId, String userId) {
    if (applicationRepository.existsByJobPostingIdAndUserId(jobId, userId)) {
      throw new BusinessException("You have already applied to this job");
    }

    JobPosting jobPosting = jobPostingRepository.findById(jobId)
                                                .orElseThrow(() -> new BusinessException("Job posting not found"));
    if (!jobPosting.getActive()) {
      throw new BusinessException("This job posting is no longer active");
    }

    Application application = applicationRepository.save(jobMapper.toApplication(jobPosting, userId));
    jobPosting.setApplicationCount(jobPosting.getApplicationCount() + 1);

    jobPostingRepository.save(jobPosting);
    jobCacheService.evict(jobId);
    return jobMapper.toApplyResponseDto(application);
  }

  @Override
  public boolean hasApplied(UUID jobId, String userId) {
    return applicationRepository.existsByJobPostingIdAndUserId(jobId, userId);
  }

  @Override
  public Page<JobPostingResponseDto> getByCity(String city, Pageable pageable) {
    return jobPostingRepository.findAll(byCityName(city), pageable)
                               .map(jobMapper::toJobPostingResponseDto);
  }

  @Override
  public Page<String> autocompletePositions(String query, Pageable pageable) {
    return jobPostingRepository.findDistinctTitlesByQuery(query, pageable);
  }

  @Override
  public Page<JobPostingResponseDto> adminSearch(AdminSearchJobsRequestDto request, Pageable pageable) {
    return jobPostingRepository.findAll(fromAdminRequest(request), pageable)
                               .map(jobMapper::toJobPostingResponseDto);
  }

  @Override
  @Transactional
  public void delete(UUID id) {
    jobPostingRepository.findById(id)
                        .orElseThrow(() -> new BusinessException("Job posting not found"));
    applicationRepository.deleteAllByJobPostingId(id);
    jobPostingRepository.deleteById(id);
    jobCacheService.evict(id);
  }
}
