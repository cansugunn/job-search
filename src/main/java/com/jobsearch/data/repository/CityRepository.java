package com.jobsearch.data.repository;

import com.jobsearch.data.entity.City;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CityRepository extends JpaRepository<City, UUID>, JpaSpecificationExecutor<City> {

}
