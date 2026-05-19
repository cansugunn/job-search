package com.jobsearch.data.dto.job.request;

import com.jobsearch.common.enums.WorkingPreference;
import java.util.UUID;

import static java.util.Objects.nonNull;
import static org.springframework.util.StringUtils.hasText;

public record SearchJobsRequestDto(String position,
                                   UUID countryId,
                                   UUID cityId,
                                   UUID townId,
                                   WorkingPreference workingPreference) {

  public boolean hasSearchCriteria() {
    return hasText(position)
           || nonNull(countryId)
           || nonNull(cityId)
           || nonNull(townId)
           || nonNull(workingPreference);
  }
}
