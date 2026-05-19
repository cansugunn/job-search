package com.jobsearch.data.dto.job.response;

import java.time.LocalDateTime;

public record RecentSearchResponseDto(String position,
                                      String town,
                                      String city,
                                      String country,
                                      String workingPreference,
                                      LocalDateTime searchedAt) {

}
