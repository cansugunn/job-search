package com.jobsearch.data.dto.job.response;

import com.jobsearch.common.enums.WorkingPreference;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record JobPostingResponseDto(UUID id,
                                    String title,
                                    String description,
                                    CompanyDto company,
                                    UUID townId,
                                    String town,
                                    UUID cityId,
                                    String city,
                                    UUID countryId,
                                    String country,
                                    WorkingPreference workingPreference,
                                    BigDecimal salary,
                                    Boolean active,
                                    Integer applicationCount,
                                    LocalDateTime lastUpdatedDate) {

}
