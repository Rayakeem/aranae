package com.aranae.api.search.service;

import com.aranae.api.celebrity.controller.dto.CelebrityResponse;
import com.aranae.api.celebrity.repository.CelebrityRepository;
import com.aranae.api.common.response.PageResponse;
import com.aranae.api.post.controller.dto.PostSummaryResponse;
import com.aranae.api.post.repository.PostRepository;
import com.aranae.api.search.controller.dto.SearchResponse;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class SearchService {

    private final CelebrityRepository celebrityRepository;
    private final PostRepository postRepository;

    public SearchService(CelebrityRepository celebrityRepository,
                         PostRepository postRepository) {
        this.celebrityRepository = celebrityRepository;
        this.postRepository = postRepository;
    }

    public SearchResponse search(String q, Pageable pageable) {
        // 1. 연예인 이름으로 검색 (최대 5개만, 카드 형태로 보여주기 위해)
        List<CelebrityResponse> celebrities = celebrityRepository
                .findByNameContainingIgnoreCase(q, PageRequest.of(0, 5))
                .map(CelebrityResponse::from)
                .getContent();

        // 2. 포스트 제목 또는 출처 채널명으로 검색
        PageResponse<PostSummaryResponse> posts = PageResponse.from(
                postRepository
                        .findByTitleContainingIgnoreCaseOrSourceChannelContainingIgnoreCase(q, q, pageable)
                        .map(PostSummaryResponse::from)
        );

        return new SearchResponse(celebrities, posts);
    }
}
