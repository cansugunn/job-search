package com.jobsearch.data.dto.job.response;

import java.util.List;

public record JobDetailResponseDto(JobPostingResponseDto posting,
                                   List<JobPostingResponseDto> relatedJobs) {

}
