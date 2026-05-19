package com.jobsearch.data.repository;

import com.jobsearch.data.entity.Town;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface TownRepository extends JpaRepository<Town, UUID>, JpaSpecificationExecutor<Town> {

}
