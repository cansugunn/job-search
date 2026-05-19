package com.jobsearch.data.mapper;

import com.jobsearch.data.dto.job.request.CreateJobRequestDto;
import com.jobsearch.data.dto.job.request.UpdateJobRequestDto;
import com.jobsearch.data.dto.job.response.ApplyResponseDto;
import com.jobsearch.data.dto.job.response.CompanyDto;
import com.jobsearch.data.dto.job.response.JobDetailResponseDto;
import com.jobsearch.data.dto.job.response.JobPostingResponseDto;
import com.jobsearch.data.entity.Application;
import com.jobsearch.data.entity.Company;
import com.jobsearch.data.entity.JobPosting;
import com.jobsearch.data.event.NewJobPostingEvent;
import java.util.List;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface JobMapper {

  @Mapping(target = "townId",    source = "town.id")
  @Mapping(target = "town",      source = "town.name")
  @Mapping(target = "cityId",    source = "town.city.id")
  @Mapping(target = "city",      source = "town.city.name")
  @Mapping(target = "countryId", source = "town.city.country.id")
  @Mapping(target = "country",   source = "town.city.country.name")
  @Mapping(target = "company",   source = "company")
  JobPostingResponseDto toJobPostingResponseDto(JobPosting jobPosting);

  CompanyDto toCompanyDto(Company company);

  @Mapping(target = "applicationId", source = "id")
  @Mapping(target = "jobPostingId", source = "jobPosting.id")
  ApplyResponseDto toApplyResponseDto(Application application);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "town", ignore = true)
  @Mapping(target = "company", ignore = true)
  @Mapping(target = "active", ignore = true)
  @Mapping(target = "applicationCount", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "lastUpdatedDate", ignore = true)
  JobPosting toJobPosting(CreateJobRequestDto dto);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "town", ignore = true)
  @Mapping(target = "company", ignore = true)
  @Mapping(target = "applicationCount", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "lastUpdatedDate", ignore = true)
  void updateEntity(UpdateJobRequestDto dto, @MappingTarget JobPosting posting);

  @Mapping(target = "jobPosting", source = "posting")
  @Mapping(target = "userId", source = "userId")
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "appliedAt", ignore = true)
  Application toApplication(JobPosting posting, String userId);

  @Mapping(target = "jobId", expression = "java(String.valueOf(posting.getId()))")
  @Mapping(target = "town", source = "town.name")
  @Mapping(target = "city", source = "town.city.name")
  @Mapping(target = "country", source = "town.city.country.name")
  @Mapping(target = "workingPreference", expression = "java(posting.getWorkingPreference().name())")
  NewJobPostingEvent toNewJobPostingEvent(JobPosting posting);

  JobDetailResponseDto toJobDetailResponseDto(JobPostingResponseDto posting, List<JobPostingResponseDto> relatedJobs);
}
