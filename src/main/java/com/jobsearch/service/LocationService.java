package com.jobsearch.service;

import com.jobsearch.data.dto.location.CityResponseDto;
import com.jobsearch.data.dto.location.CompanyResponseDto;
import com.jobsearch.data.dto.location.CountryResponseDto;
import com.jobsearch.data.dto.location.TownResponseDto;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LocationService {

  Page<CityResponseDto> getCities(String query, UUID countryId, @NotNull Pageable pageable);

  Page<CountryResponseDto> getCountries(@NotNull Pageable pageable);

  Page<CompanyResponseDto> getCompanies(@NotNull Pageable pageable);

  Page<TownResponseDto> getTowns(String query, UUID cityId, @NotNull Pageable pageable);
}
