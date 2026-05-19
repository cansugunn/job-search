package com.jobsearch.data.dto.job.request;

import com.jobsearch.common.enums.WorkingPreference;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.UUID;

public record CreateJobRequestDto(@NotBlank String title,
                                  @NotBlank String description,
                                  @NotNull UUID companyId,
                                  @NotNull UUID townId,
                                  @NotNull WorkingPreference workingPreference,
                                  @NotNull @Positive BigDecimal salary) {

}
