package com.jobsearch.data.repository.specification;

import com.jobsearch.common.enums.WorkingPreference;
import com.jobsearch.data.dto.job.request.AdminSearchJobsRequestDto;
import com.jobsearch.data.dto.job.request.SearchJobsRequestDto;
import com.jobsearch.data.entity.City_;
import com.jobsearch.data.entity.Country_;
import com.jobsearch.data.entity.JobPosting;
import com.jobsearch.data.entity.Town_;
import jakarta.persistence.criteria.Predicate;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.domain.Specification;

import static com.jobsearch.data.entity.City_.COUNTRY;
import static com.jobsearch.data.entity.JobPosting_.ACTIVE;
import static com.jobsearch.data.entity.JobPosting_.TITLE;
import static com.jobsearch.data.entity.JobPosting_.TOWN;
import static com.jobsearch.data.entity.JobPosting_.WORKING_PREFERENCE;
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
        predicates.add(criteriaBuilder.like(
            criteriaBuilder.lower(root.get(TITLE)), "%" + position.toLowerCase() + "%"));
      }

      UUID townId = request.townId();
      if (nonNull(townId)) {
        predicates.add(criteriaBuilder.equal(root.get(TOWN).get(Town_.ID), townId));
      }

      UUID cityId = request.cityId();
      if (nonNull(cityId)) {
        predicates.add(criteriaBuilder.equal(root.get(TOWN).get(CITY).get(City_.ID), cityId));
      }

      UUID countryId = request.countryId();
      if (nonNull(countryId)) {
        predicates.add(criteriaBuilder.equal(root.get(TOWN).get(CITY).get(COUNTRY).get(Country_.ID), countryId));
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
