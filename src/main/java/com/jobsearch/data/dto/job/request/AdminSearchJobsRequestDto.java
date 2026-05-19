package com.jobsearch.data.dto.job.request;

import com.jobsearch.common.enums.WorkingPreference;

public record AdminSearchJobsRequestDto(String title,
                                        WorkingPreference workingPreference,
                                        Boolean active) {
}
