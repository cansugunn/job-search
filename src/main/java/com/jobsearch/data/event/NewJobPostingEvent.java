package com.jobsearch.data.event;

public record NewJobPostingEvent(String jobId,
                                 String title,
                                 String town,
                                 String city,
                                 String country,
                                 String workingPreference) {

}
