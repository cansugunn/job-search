package com.jobsearch.controller;

import com.jobsearch.data.dto.job.response.RecentSearchResponseDto;
import com.jobsearch.service.SearchHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/searches")
@RequiredArgsConstructor
@Tag(name = "Search History",
     description = "User recent search history")
@SecurityRequirement(name = "bearerAuth")
public class SearchHistoryController {

  private final SearchHistoryService searchHistoryService;

  @Operation(summary = "Get recent searches of the authenticated user")
  @GetMapping("/recent")
  public ResponseEntity<Page<RecentSearchResponseDto>>
  getRecentSearches(@AuthenticationPrincipal Jwt jwt,
                    @ParameterObject @PageableDefault(size = 5) Pageable pageable) {
    return ResponseEntity.ok(searchHistoryService.getRecentSearches(jwt.getSubject(), pageable));
  }
}
