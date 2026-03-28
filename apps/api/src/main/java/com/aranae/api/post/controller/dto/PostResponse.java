package com.aranae.api.post.controller.dto;

import com.aranae.api.celebrity.domain.Celebrity;
import com.aranae.api.celebrity.domain.JobType;
import com.aranae.api.item.controller.dto.ItemResponse;
import com.aranae.api.post.domain.Post;

import java.time.LocalDateTime;
import java.util.List;

// 상세 페이지용 (아이템 전체 포함)
public record PostResponse(
        Long id,
        String title,
        CelebrityInfo celebrity,
        String celebrityImageKey,
        String sourceChannel,
        String sourceUrl,
        List<String> tags,
        String createdByNickname,
        List<ItemResponse> items,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static PostResponse from(Post post) {
        return new PostResponse(
                post.getId(),
                post.getTitle(),
                CelebrityInfo.from(post.getCelebrity()),
                post.getCelebrityImageKey(),
                post.getSourceChannel(),
                post.getSourceUrl(),
                post.getTags(),
                post.getCreatedBy() != null ? post.getCreatedBy().getNickname() : null,
                post.getPostItems().stream()
                        .map(pi -> ItemResponse.from(pi.getItem()))
                        .toList(),
                post.getCreatedAt(),
                post.getUpdatedAt()
        );
    }

    public record CelebrityInfo(Long id, String name, String groupName, JobType jobType, String profileImageKey) {
        public static CelebrityInfo from(Celebrity celebrity) {
            return new CelebrityInfo(
                    celebrity.getId(),
                    celebrity.getName(),
                    celebrity.getGroupName(),
                    celebrity.getJobType(),
                    celebrity.getProfileImageKey()
            );
        }
    }
}
