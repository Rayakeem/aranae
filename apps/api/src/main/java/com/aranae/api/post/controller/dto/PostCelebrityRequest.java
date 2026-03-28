package com.aranae.api.post.controller.dto;

import com.aranae.api.celebrity.domain.JobType;
import jakarta.validation.constraints.NotBlank;

public record PostCelebrityRequest(
        @NotBlank String name,
        String groupName,
        JobType jobType
) {}
