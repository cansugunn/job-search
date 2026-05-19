package com.jobsearch.service.impl;

import com.jobsearch.common.enums.WorkingPreference;
import com.jobsearch.data.document.JobSearch;
import com.jobsearch.data.dto.job.request.SearchJobsRequestDto;
import com.jobsearch.data.dto.job.response.JobSearchResponseDto;
import com.jobsearch.data.dto.job.response.RecentSearchResponseDto;
import com.jobsearch.data.entity.City;
import com.jobsearch.data.entity.Country;
import com.jobsearch.data.entity.Town;
import com.jobsearch.data.mapper.SearchMapper;
import com.jobsearch.data.repository.CityRepository;
import com.jobsearch.data.repository.CountryRepository;
import com.jobsearch.data.repository.JobSearchRepository;
import com.jobsearch.data.repository.TownRepository;
import com.jobsearch.service.SearchHistoryService;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import static java.util.Optional.ofNullable;

@Validated
@Service
@RequiredArgsConstructor
public class SearchHistoryServiceImpl implements SearchHistoryService {

  private final JobSearchRepository jobSearchRepository;

  private final TownRepository townRepository;

  private final CityRepository cityRepository;

  private final CountryRepository countryRepository;

  private final SearchMapper searchMapper;

  @Override
  public void saveSearch(String userId, SearchJobsRequestDto request) {
    if (!request.hasSearchCriteria()) {
      return;
    }

    String townName = ofNullable(request.townId())
        .flatMap(townRepository::findById)
        .map(Town::getName)
        .orElse(null);
    String cityName = ofNullable(request.cityId())
        .flatMap(cityRepository::findById)
        .map(City::getName)
        .orElse(null);
    String countryName = ofNullable(request.countryId())
        .flatMap(countryRepository::findById)
        .map(Country::getName)
        .orElse(null);

    JobSearch jobSearch = JobSearch.builder()
                                   .userId(userId)
                                   .position(request.position())
                                   .town(townName)
                                   .city(cityName)
                                   .country(countryName)
                                   .workingPreference(ofNullable(request.workingPreference())
                                                          .map(WorkingPreference::name)
                                                          .orElse(null))
                                   .build();
    jobSearchRepository.save(jobSearch);
  }

  @Override
  public Page<RecentSearchResponseDto> getRecentSearches(String userId, Pageable pageable) {
    return jobSearchRepository.findByUserIdOrderBySearchedAtDesc(userId, pageable)
                              .map(searchMapper::toRecentSearchDto);
  }

  @Override
  public Page<JobSearchResponseDto> getSearchesSince(int days, Pageable pageable) {
    return jobSearchRepository.findBySearchedAtAfterOrderBySearchedAtDesc(
                                  LocalDateTime.now().minusDays(days),
                                  pageable)
                              .map(searchMapper::toJobSearchDto);
  }
}
