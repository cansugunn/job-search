package com.jobsearch.data.dto.job.response;

import java.util.UUID;

public record CompanyDto(UUID id,
                         String name,
                         String website) {

}
