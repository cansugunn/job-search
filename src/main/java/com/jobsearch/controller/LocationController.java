package com.jobsearch.controller;

import com.jobsearch.data.dto.location.CityResponseDto;
import com.jobsearch.data.dto.location.CompanyResponseDto;
import com.jobsearch.data.dto.location.CountryResponseDto;
import com.jobsearch.data.dto.location.TownResponseDto;
import com.jobsearch.service.LocationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Locations", description = "Cities, countries, and companies with autocomplete support")
public class LocationController {

  private final LocationService locationService;

  @Operation(summary = "Get cities, optionally filtered by country and/or name")
  @GetMapping("/api/v1/cities")
  public ResponseEntity<Page<CityResponseDto>> getCities(
      @RequestParam(required = false) String query,
      @RequestParam(required = false) UUID countryId,
      @ParameterObject @PageableDefault(size = 20) Pageable pageable) {
    return ResponseEntity.ok(locationService.getCities(query, countryId, pageable));
  }

  @Operation(summary = "Get all countries")
  @GetMapping("/api/v1/countries")
  public ResponseEntity<Page<CountryResponseDto>> getCountries(
      @ParameterObject @PageableDefault(size = 20) Pageable pageable) {
    return ResponseEntity.ok(locationService.getCountries(pageable));
  }

  @Operation(summary = "Get all companies")
  @GetMapping("/api/v1/companies")
  public ResponseEntity<Page<CompanyResponseDto>> getCompanies(
      @ParameterObject @PageableDefault(size = 20) Pageable pageable) {
    return ResponseEntity.ok(locationService.getCompanies(pageable));
  }

  @Operation(summary = "Get towns, optionally filtered by city and/or name")
  @GetMapping("/api/v1/towns")
  public ResponseEntity<Page<TownResponseDto>> getTowns(
      @RequestParam(required = false) String query,
      @RequestParam(required = false) UUID cityId,
      @ParameterObject @PageableDefault(size = 20) Pageable pageable) {
    return ResponseEntity.ok(locationService.getTowns(query, cityId, pageable));
  }
}
