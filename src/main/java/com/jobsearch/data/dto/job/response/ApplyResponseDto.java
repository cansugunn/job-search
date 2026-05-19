package com.jobsearch.data.dto.job.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record ApplyResponseDto(UUID applicationId,
                               UUID jobPostingId,
                               LocalDateTime appliedAt) {

}
