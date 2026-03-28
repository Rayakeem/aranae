package com.aranae.api.celebrity.controller.dto;

import com.aranae.api.celebrity.domain.Celebrity;
import com.aranae.api.celebrity.domain.JobType;

import java.time.LocalDateTime;

public record CelebrityResponse(
        Long id,
        String name,
        String groupName,
        JobType jobType,
        Integer height,
        String profileImageKey,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static CelebrityResponse from(Celebrity celebrity) {
        return new CelebrityResponse(
                celebrity.getId(),
                celebrity.getName(),
                celebrity.getGroupName(),
                celebrity.getJobType(),
                celebrity.getHeight(),
                celebrity.getProfileImageKey(),
                celebrity.getCreatedAt(),
                celebrity.getUpdatedAt()
        );
    }
}
