package com.jobsearch.data.dto.location;

import java.util.UUID;

public record CompanyResponseDto(UUID id,
                                 String name,
                                 String website,
                                 String description) {

}
