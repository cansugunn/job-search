package com.jobsearch.data.repository;

import com.jobsearch.data.entity.Application;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, UUID> {

  boolean existsByJobPostingIdAndUserId(UUID jobPostingId, String userId);

  void deleteAllByJobPostingId(UUID jobPostingId);
}
