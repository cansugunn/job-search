package com.jobsearch.data.repository.specification;

import com.jobsearch.data.entity.City;
import com.jobsearch.data.entity.City_;
import jakarta.persistence.criteria.Predicate;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.domain.Specification;

import static com.jobsearch.data.entity.City_.ID;
import static com.jobsearch.data.entity.City_.NAME;
import static java.util.Objects.nonNull;
import static org.springframework.util.StringUtils.hasText;

public class CitySpecification {

  public static Specification<City> fromFilters(String query, UUID countryId) {
    return (root, criteriaQuery, criteriaBuilder) -> {
      List<Predicate> predicates = new LinkedList<>();

      if (hasText(query)) {
        predicates.add(
            criteriaBuilder.like(criteriaBuilder.lower(root.get(NAME)), "%" + query.toLowerCase() + "%"));
      }

      if (nonNull(countryId)) {
        predicates.add(criteriaBuilder.equal(root.get(City_.country).get(ID), countryId));
      }

      return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    };
  }
}
