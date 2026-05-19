package com.jobsearch.data.dto.location;

import java.util.UUID;

public record CityResponseDto(UUID id,
                              String name,
                              CountryResponseDto country) {

}
