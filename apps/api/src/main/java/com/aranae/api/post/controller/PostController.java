package com.aranae.api.post.controller;

import com.aranae.api.common.response.PageResponse;
import com.aranae.api.post.controller.dto.*;
import com.aranae.api.post.service.PostService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    // 메인 피드 (검색어 q 또는 태그 tag로 필터)
    // GET /posts
    // GET /posts?q=뉴진스
    // GET /posts?q=만약에+우리   ← 드라마명 검색
    // GET /posts?tag=여자아이돌  ← 해시태그 필터
    @GetMapping
    public PageResponse<PostSummaryResponse> findAll(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String tag,
            @PageableDefault(size = 10) Pageable pageable) {
        return PageResponse.from(postService.findAll(q, tag, pageable));
    }

    // 상세 페이지
    @GetMapping("/{id}")
    public PostResponse findById(@PathVariable Long id) {
        return postService.findById(id);
    }

    // 포스트 등록 (등록 페이지 "포스트하기")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PostResponse create(@RequestBody @Valid PostCreateRequest request) {
        return postService.create(request);
    }

    // 포스트 수정 (상세 페이지 "정보 수정하기")
    @PutMapping("/{id}")
    public PostResponse update(@PathVariable Long id,
                               @RequestBody @Valid PostUpdateRequest request) {
        return postService.update(id, request);
    }

    // 아이템 추가 (상세 페이지 "정보 추가하기")
    @PostMapping("/{id}/items")
    @ResponseStatus(HttpStatus.CREATED)
    public PostResponse addItem(@PathVariable Long id,
                                @RequestBody @Valid PostItemRequest request) {
        return postService.addItem(id, request);
    }

    // 아이템 제거
    @DeleteMapping("/{id}/items/{itemId}")
    public PostResponse removeItem(@PathVariable Long id, @PathVariable Long itemId) {
        return postService.removeItem(id, itemId);
    }

    // 포스트 삭제
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        postService.delete(id);
    }
}
