package com.aranae.api.post.service;

import com.aranae.api.celebrity.domain.Celebrity;
import com.aranae.api.celebrity.repository.CelebrityRepository;
import com.aranae.api.item.domain.Item;
import com.aranae.api.item.repository.ItemRepository;
import com.aranae.api.post.controller.dto.*;
import com.aranae.api.post.domain.Post;
import com.aranae.api.common.exception.NotFoundException;
import com.aranae.api.post.repository.PostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final CelebrityRepository celebrityRepository;
    private final ItemRepository itemRepository;

    public PostService(PostRepository postRepository,
                       CelebrityRepository celebrityRepository,
                       ItemRepository itemRepository) {
        this.postRepository = postRepository;
        this.celebrityRepository = celebrityRepository;
        this.itemRepository = itemRepository;
    }

    // 전체 피드 (검색어 또는 태그 필터)
    public Page<PostSummaryResponse> findAll(String q, String tag, Pageable pageable) {
        if (tag != null && !tag.isBlank()) {
            return postRepository.findByTagContaining(tag, pageable)
                    .map(PostSummaryResponse::from);
        }
        if (q != null && !q.isBlank()) {
            // 연예인 이름 또는 제목/채널명으로 통합 검색
            return postRepository
                    .findByTitleContainingIgnoreCaseOrSourceChannelContainingIgnoreCase(q, q, pageable)
                    .map(PostSummaryResponse::from);
        }
        return postRepository.findAll(pageable).map(PostSummaryResponse::from);
    }

    public PostResponse findById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 포스트입니다. id=" + id));
        return PostResponse.from(post);
    }

    @Transactional
    public PostResponse create(PostCreateRequest request) {
        // 1. 연예인 찾기 (없으면 자동 생성 - 위키 방식)
        Celebrity celebrity = findOrCreateCelebrity(request.celebrity());

        // 2. 포스트 생성
        Post post = Post.create(
                request.title(), celebrity, request.celebrityImageKey(),
                request.sourceChannel(), request.sourceUrl(), request.tags()
        );

        // 3. 아이템 찾기 (없으면 자동 생성) + 포스트에 연결
        for (PostItemRequest itemRequest : request.items()) {
            Item item = findOrCreateItem(itemRequest);
            post.addItem(item);
        }

        return PostResponse.from(postRepository.save(post));
    }

    @Transactional
    public PostResponse update(Long id, PostUpdateRequest request) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 포스트입니다. id=" + id));
        post.update(request.title(), request.sourceChannel(), request.sourceUrl(), request.tags());
        return PostResponse.from(post);
    }

    // 포스트에 아이템 추가 (상세 페이지 "정보 추가하기")
    @Transactional
    public PostResponse addItem(Long postId, PostItemRequest request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 포스트입니다. id=" + postId));
        Item item = findOrCreateItem(request);
        post.addItem(item);
        return PostResponse.from(post);
    }

    // 포스트에서 아이템 제거
    @Transactional
    public PostResponse removeItem(Long postId, Long itemId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 포스트입니다. id=" + postId));
        post.removeItem(itemId);
        return PostResponse.from(post);
    }

    @Transactional
    public void delete(Long id) {
        if (!postRepository.existsById(id)) {
            throw new NotFoundException("존재하지 않는 포스트입니다. id=" + id);
        }
        postRepository.deleteById(id);
    }

    // 연예인 findOrCreate: 이름+그룹 일치하는 연예인이 없으면 새로 등록
    private Celebrity findOrCreateCelebrity(PostCelebrityRequest request) {
        if (request.groupName() != null && !request.groupName().isBlank()) {
            return celebrityRepository
                    .findFirstByNameIgnoreCaseAndGroupNameIgnoreCase(request.name(), request.groupName())
                    .orElseGet(() -> celebrityRepository.save(
                            Celebrity.create(request.name(), request.groupName(), request.jobType(), null)
                    ));
        }
        return celebrityRepository
                .findFirstByNameIgnoreCase(request.name())
                .orElseGet(() -> celebrityRepository.save(
                        Celebrity.create(request.name(), null, request.jobType(), null)
                ));
    }

    // 아이템 findOrCreate: uniqueKey 일치하는 아이템이 없으면 새로 등록
    private Item findOrCreateItem(PostItemRequest request) {
        String uniqueKey = normalize(request.brand()) + "|"
                + normalize(request.name()) + "|"
                + normalize(request.category());
        return itemRepository.findByUniqueKey(uniqueKey)
                .orElseGet(() -> itemRepository.save(
                        Item.create(request.name(), request.brand(), request.category(),
                                request.price(), request.imageKey(),
                                request.purchaseUrl(), request.affiliateUrl())
                ));
    }

    private String normalize(String v) {
        if (v == null) return "";
        return v.trim().toLowerCase().replaceAll("\\s+", "-");
    }
}
