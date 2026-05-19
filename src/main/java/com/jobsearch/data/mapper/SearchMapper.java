package com.jobsearch.data.mapper;

import com.jobsearch.data.document.JobSearch;
import com.jobsearch.data.dto.job.response.JobSearchResponseDto;
import com.jobsearch.data.dto.job.response.RecentSearchResponseDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SearchMapper {

  RecentSearchResponseDto toRecentSearchDto(JobSearch jobSearch);

  JobSearchResponseDto toJobSearchDto(JobSearch jobSearch);
}
