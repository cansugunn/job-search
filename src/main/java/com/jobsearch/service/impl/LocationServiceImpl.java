package com.jobsearch.service.impl;

import com.jobsearch.data.dto.location.CityResponseDto;
import com.jobsearch.data.dto.location.CompanyResponseDto;
import com.jobsearch.data.dto.location.CountryResponseDto;
import com.jobsearch.data.dto.location.TownResponseDto;
import com.jobsearch.data.mapper.LocationMapper;
import com.jobsearch.data.repository.CityRepository;
import com.jobsearch.data.repository.CompanyRepository;
import com.jobsearch.data.repository.CountryRepository;
import com.jobsearch.data.repository.TownRepository;
import com.jobsearch.data.repository.specification.CitySpecification;
import com.jobsearch.data.repository.specification.TownSpecification;
import com.jobsearch.service.LocationService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {

  private final CityRepository cityRepository;

  private final CountryRepository countryRepository;

  private final CompanyRepository companyRepository;

  private final TownRepository townRepository;

  private final LocationMapper locationMapper;

  @Override
  public Page<CityResponseDto> getCities(String query, UUID countryId, Pageable pageable) {
    return cityRepository.findAll(CitySpecification.fromFilters(query, countryId), pageable)
                         .map(locationMapper::toCityResponseDto);
  }

  @Override
  public Page<CountryResponseDto> getCountries(Pageable pageable) {
    return countryRepository.findAll(pageable)
                            .map(locationMapper::toCountryResponseDto);
  }

  @Override
  public Page<CompanyResponseDto> getCompanies(Pageable pageable) {
    return companyRepository.findAll(pageable)
                            .map(locationMapper::toCompanyResponseDto);
  }

  @Override
  public Page<TownResponseDto> getTowns(String query, UUID cityId, Pageable pageable) {
    return townRepository.findAll(TownSpecification.fromFilters(query, cityId), pageable)
                         .map(locationMapper::toTownResponseDto);
  }
}
