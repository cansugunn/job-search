package com.jobsearch.data.document;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "job_searches")
@Getter
@Setter
@Builder
public class JobSearch {

  @Id
  private String id;

  @Indexed
  private String userId;

  private String position;

  private String town;

  private String city;

  private String country;

  private String workingPreference;

  @CreatedDate
  @Indexed
  private LocalDateTime searchedAt;
}
