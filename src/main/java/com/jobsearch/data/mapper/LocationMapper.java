package com.jobsearch.data.mapper;

import com.jobsearch.data.dto.location.CityResponseDto;
import com.jobsearch.data.dto.location.CompanyResponseDto;
import com.jobsearch.data.dto.location.CountryResponseDto;
import com.jobsearch.data.dto.location.TownResponseDto;
import com.jobsearch.data.entity.City;
import com.jobsearch.data.entity.Company;
import com.jobsearch.data.entity.Country;
import com.jobsearch.data.entity.Town;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LocationMapper {

  CityResponseDto toCityResponseDto(City city);

  CountryResponseDto toCountryResponseDto(Country country);

  CompanyResponseDto toCompanyResponseDto(Company company);

  TownResponseDto toTownResponseDto(Town town);
}
