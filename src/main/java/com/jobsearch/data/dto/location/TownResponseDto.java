package com.jobsearch.data.dto.location;

import java.util.UUID;

public record TownResponseDto(UUID id,
                              String name,
                              CityResponseDto city) {

}
