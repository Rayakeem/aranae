package com.aranae.api.post.repository;

import com.aranae.api.post.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, Long> {

    // 연예인 이름으로 검색
    Page<Post> findByCelebrity_NameContainingIgnoreCase(String name, Pageable pageable);

    // 제목 또는 출처 채널명으로 검색 ("만약에 우리" 같은 드라마명)
    Page<Post> findByTitleContainingIgnoreCaseOrSourceChannelContainingIgnoreCase(
            String title, String sourceChannel, Pageable pageable);

    // 해시태그 필터 (JSON 컬럼 LIKE 검색, MVP)
    @Query("SELECT p FROM Post p WHERE p.tags LIKE %:tag%")
    Page<Post> findByTagContaining(@Param("tag") String tag, Pageable pageable);
}
