package com.aranae.api.post.domain;

import com.aranae.api.item.domain.Item;
import jakarta.persistence.*;

@Entity
@Table(
        name = "post_items",
        uniqueConstraints = @UniqueConstraint(columnNames = {"post_id", "item_id"})
)
public class PostItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(optional = false)
    @JoinColumn(name = "item_id")
    private Item item;

    protected PostItem() {
    }

    public static PostItem create(Post post, Item item) {
        PostItem postItem = new PostItem();
        postItem.post = post;
        postItem.item = item;
        return postItem;
    }

    public Long getId() { return id; }
    public Post getPost() { return post; }
    public Item getItem() { return item; }
}
