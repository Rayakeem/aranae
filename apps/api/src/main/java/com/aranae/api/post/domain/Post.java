package com.aranae.api.post.domain;

import com.aranae.api.celebrity.domain.Celebrity;
import com.aranae.api.common.domain.BaseTimeEntity;
import com.aranae.api.common.domain.StringListConverter;
import com.aranae.api.item.domain.Item;
import com.aranae.api.user.domain.User;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "posts")
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @ManyToOne(optional = false)
    @JoinColumn(name = "celebrity_id")
    private Celebrity celebrity;

    // 포스트의 대표 이미지 (연예인 사진, S3 키)
    private String celebrityImageKey;

    // 원본 출처 채널명 ("뉴진스 인스타그램", "New Jeans Youtube")
    private String sourceChannel;

    private String sourceUrl;

    // 해시태그 목록 ["여자아이돌", "공항패션"] → JSON으로 저장
    @Convert(converter = StringListConverter.class)
    @Column(columnDefinition = "json")
    private List<String> tags = new ArrayList<>();

    // 게시자 (Auth 구현 전까지 nullable)
    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostItem> postItems = new ArrayList<>();

    protected Post() {
    }

    public static Post create(String title, Celebrity celebrity, String celebrityImageKey,
                              String sourceChannel, String sourceUrl, List<String> tags) {
        Post post = new Post();
        post.title = title;
        post.celebrity = celebrity;
        post.celebrityImageKey = celebrityImageKey;
        post.sourceChannel = sourceChannel;
        post.sourceUrl = sourceUrl;
        post.tags = tags != null ? tags : new ArrayList<>();
        return post;
    }

    public void addItem(Item item) {
        PostItem postItem = PostItem.create(this, item);
        this.postItems.add(postItem);
    }

    public void removeItem(Long itemId) {
        postItems.removeIf(pi -> pi.getItem().getId().equals(itemId));
    }

    public void update(String title, String sourceChannel, String sourceUrl, List<String> tags) {
        this.title = title;
        this.sourceChannel = sourceChannel;
        this.sourceUrl = sourceUrl;
        this.tags = tags != null ? tags : new ArrayList<>();
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public Celebrity getCelebrity() { return celebrity; }
    public String getCelebrityImageKey() { return celebrityImageKey; }
    public String getSourceChannel() { return sourceChannel; }
    public String getSourceUrl() { return sourceUrl; }
    public List<String> getTags() { return tags; }
    public User getCreatedBy() { return createdBy; }
    public List<PostItem> getPostItems() { return postItems; }
}
