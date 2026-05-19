package com.jobsearch.data.repository.specification;

import com.jobsearch.common.enums.WorkingPreference;
import com.jobsearch.data.dto.job.request.AdminSearchJobsRequestDto;
import com.jobsearch.data.dto.job.request.SearchJobsRequestDto;
import com.jobsearch.data.entity.City_;
import com.jobsearch.data.entity.Country_;
import com.jobsearch.data.entity.JobPosting;
import com.jobsearch.data.entity.Town_;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import static com.jobsearch.data.entity.City_.COUNTRY;
import static com.jobsearch.data.entity.JobPosting_.*;
import static com.jobsearch.data.entity.Town_.CITY;
import static java.util.Objects.nonNull;
import static org.springframework.util.StringUtils.hasText;

public class JobPostingSpecification {

  public static Specification<JobPosting> fromRequest(SearchJobsRequestDto request) {
    return (root, criteriaQuery, criteriaBuilder) -> {
      List<Predicate> predicates = new LinkedList<>();

      predicates.add(criteriaBuilder.isTrue(root.get(ACTIVE)));

      String position = request.position();
      if (hasText(position)) {
        String positionPattern = "%" + position.toLowerCase() + "%";
        predicates.add(criteriaBuilder.or(
                criteriaBuilder.like(criteriaBuilder.lower(root.get(TITLE)), positionPattern),
                criteriaBuilder.like(criteriaBuilder.lower(root.get(DESCRIPTION)), positionPattern)));
      }

      UUID townId = request.townId();
      if (nonNull(townId)) {
        predicates.add(criteriaBuilder.equal(root.get(TOWN).get(Town_.ID), townId));
      }

      String townName = request.townName();
      if (hasText(townName)) {
        predicates.add(criteriaBuilder.like(
                criteriaBuilder.lower(root.get(TOWN).get(Town_.NAME)), "%" + townName.toLowerCase() + "%"));
      }

      UUID cityId = request.cityId();
      if (nonNull(cityId)) {
        predicates.add(criteriaBuilder.equal(root.get(TOWN).get(CITY).get(City_.ID), cityId));
      }

      String cityName = request.cityName();
      if (hasText(cityName)) {
        predicates.add(criteriaBuilder.like(
                criteriaBuilder.lower(root.get(TOWN).get(CITY).get(City_.NAME)), "%" + cityName.toLowerCase() + "%"));
      }

      UUID countryId = request.countryId();
      if (nonNull(countryId)) {
        predicates.add(criteriaBuilder.equal(root.get(TOWN).get(CITY).get(COUNTRY).get(Country_.ID), countryId));
      }

      String countryName = request.countryName();
      if (hasText(countryName)) {
        predicates.add(criteriaBuilder.like(
                criteriaBuilder.lower(root.get(TOWN).get(CITY).get(COUNTRY).get(Country_.NAME)),
                "%" + countryName.toLowerCase() + "%"));
      }

      WorkingPreference workingPreference = request.workingPreference();
      if (nonNull(workingPreference)) {
        predicates.add(criteriaBuilder.equal(root.get(WORKING_PREFERENCE), workingPreference));
      }

      return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    };
  }

  public static Specification<JobPosting> fromAdminRequest(AdminSearchJobsRequestDto request) {
    return (root, criteriaQuery, criteriaBuilder) -> {
      List<Predicate> predicates = new LinkedList<>();

      if (nonNull(request.active())) {
        predicates.add(request.active()
                ? criteriaBuilder.isTrue(root.get(ACTIVE))
                : criteriaBuilder.isFalse(root.get(ACTIVE)));
      }

      if (hasText(request.title())) {
        predicates.add(criteriaBuilder.like(
                criteriaBuilder.lower(root.get(TITLE)), "%" + request.title().toLowerCase() + "%"));
      }

      if (nonNull(request.workingPreference())) {
        predicates.add(criteriaBuilder.equal(root.get(WORKING_PREFERENCE), request.workingPreference()));
      }

      return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    };
  }

  public static Specification<JobPosting> byCityName(String cityName) {
    return (root, criteriaQuery, criteriaBuilder) -> {
      List<Predicate> predicates = new LinkedList<>();

      predicates.add(criteriaBuilder.isTrue(root.get(ACTIVE)));

      if (hasText(cityName)) {
        predicates.add(
                criteriaBuilder.like(
                        criteriaBuilder.lower(
                                root.get(TOWN).get(CITY).get(City_.NAME)),
                        "%" + cityName.toLowerCase() + "%"));
      }

      return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    };
  }
}
