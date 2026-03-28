package com.aranae.api.search.controller.dto;

import com.aranae.api.celebrity.controller.dto.CelebrityResponse;
import com.aranae.api.common.response.PageResponse;
import com.aranae.api.post.controller.dto.PostSummaryResponse;

import java.util.List;

public record SearchResponse(
        List<CelebrityResponse> celebrities,   // 연예인 프로필 카드 (상단)
        PageResponse<PostSummaryResponse> posts // 관련 포스트 목록 (하단)
) {}
