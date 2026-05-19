package com.jobsearch.data.repository;

import com.jobsearch.data.document.JobSearch;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobSearchRepository extends MongoRepository<JobSearch, String> {

  Page<JobSearch> findByUserIdOrderBySearchedAtDesc(String userId, Pageable pageable);

  Page<JobSearch> findBySearchedAtAfterOrderBySearchedAtDesc(LocalDateTime since, Pageable pageable);
}
