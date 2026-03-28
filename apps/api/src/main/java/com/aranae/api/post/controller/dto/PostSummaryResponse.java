package com.aranae.api.post.controller.dto;

import com.aranae.api.post.domain.Post;

import java.time.LocalDateTime;
import java.util.List;

// 메인 피드 목록용 (상세 정보 제외, 가볍게)
public record PostSummaryResponse(
        Long id,
        String title,
        String celebrityName,
        String groupName,
        String celebrityImageKey,
        List<String> tags,
        int itemCount,
        LocalDateTime createdAt
) {
    public static PostSummaryResponse from(Post post) {
        return new PostSummaryResponse(
                post.getId(),
                post.getTitle(),
                post.getCelebrity().getName(),
                post.getCelebrity().getGroupName(),
                post.getCelebrityImageKey(),
                post.getTags(),
                post.getPostItems().size(),
                post.getCreatedAt()
        );
    }
}
