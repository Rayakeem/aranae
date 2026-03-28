package com.aranae.api.celebrity.controller.dto;

import com.aranae.api.celebrity.domain.JobType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record CelebrityCreateRequest(
        @NotBlank String name,
        String groupName,
        JobType jobType,
        @Min(100) @Max(250) Integer height
) {}
