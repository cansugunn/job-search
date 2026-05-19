package com.jobsearch.data.dto.job.request;

import com.jobsearch.common.enums.WorkingPreference;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.UUID;

public record UpdateJobRequestDto(@NotEmpty String title,
                                  @NotEmpty String description,
                                  @NotNull UUID townId,
                                  @NotNull WorkingPreference workingPreference,
                                  @NotNull @Positive BigDecimal salary,
                                  @NotNull Boolean active) {

}
