package com.jobsearch.data.repository;

import com.jobsearch.data.entity.JobPosting;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JobPostingRepository extends JpaRepository<JobPosting, UUID>,
                                              JpaSpecificationExecutor<JobPosting> {

  @Query("""
      SELECT j FROM JobPosting j
      WHERE j.active = true
      AND j.id <> :excludeId
      AND (j.town.city.id = :cityId OR LOWER(j.title) LIKE LOWER(CONCAT('%', :keyword, '%')))
      ORDER BY j.lastUpdatedDate DESC
      """)
  List<JobPosting> findRelated(UUID excludeId, UUID cityId, String keyword, Pageable pageable);

  @Query(
      value = """
          SELECT j
          FROM JobPosting j
          WHERE j.active = true
          AND j.id != :excludeId
          AND (j.town.city.id = :cityId OR LOWER(j.title) LIKE LOWER(CONCAT('%', :keyword, '%')))
          ORDER BY j.lastUpdatedDate DESC
          """,
      countQuery = """
          SELECT COUNT(j)
          FROM JobPosting j
          WHERE j.active = true
          AND j.id != :excludeId
          AND (j.town.city.id = :cityId OR LOWER(j.title) LIKE LOWER(CONCAT('%', :keyword, '%')))
          """)
  Page<JobPosting> findRelatedPage(@Param("excludeId") UUID excludeId,
                                   @Param("cityId") UUID cityId,
                                   @Param("keyword") String keyword,
                                   Pageable pageable);

  @Query(
      value = """
          SELECT DISTINCT j.title
          FROM JobPosting j
          WHERE j.active = true
            AND LOWER(j.title) LIKE LOWER(CONCAT('%', :query, '%'))
          """,
      countQuery = """
          SELECT COUNT(DISTINCT j.title)
          FROM JobPosting j
          WHERE j.active = true
            AND LOWER(j.title) LIKE LOWER(CONCAT('%', :query, '%'))
          """)
  Page<String> findDistinctTitlesByQuery(@Param("query") String query, Pageable pageable);
}
