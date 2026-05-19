package com.jobsearch.data.repository.specification;

import com.jobsearch.data.entity.City_;
import com.jobsearch.data.entity.Town;
import jakarta.persistence.criteria.Predicate;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.domain.Specification;

import static com.jobsearch.data.entity.Town_.CITY;
import static com.jobsearch.data.entity.Town_.NAME;
import static java.util.Objects.nonNull;
import static org.springframework.util.StringUtils.hasText;

public class TownSpecification {

  public static Specification<Town> fromFilters(String query, UUID cityId) {
    return (root, criteriaQuery, criteriaBuilder) -> {
      List<Predicate> predicates = new LinkedList<>();

      if (hasText(query)) {
        predicates.add(criteriaBuilder.like(
            criteriaBuilder.lower(root.get(NAME)),
            "%" + query.toLowerCase() + "%"));
      }

      if (nonNull(cityId)) {
        predicates.add(criteriaBuilder.equal(root.get(CITY).get(City_.ID), cityId));
      }

      return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    };
  }
}
