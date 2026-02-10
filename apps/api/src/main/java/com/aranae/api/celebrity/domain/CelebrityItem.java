package com.aranae.api.celebrity.domain;

import com.aranae.api.common.domain.BaseTimeEntity;
import com.aranae.api.item.domain.Item;
import jakarta.persistence.*;

@Entity
@Table(
        name = "celebrity_items",
        uniqueConstraints = @UniqueConstraint(columnNames = {"celebrity_id", "item_id"})
)
public class CelebrityItem extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "celebrity_id")
    private Celebrity celebrity;

    @ManyToOne(optional = false)
    @JoinColumn(name = "item_id")
    private Item item;

    private String contentType;
    private String contentTitle;
    private String channelName;
    private String episode;
    private String sourceUrl;

    protected CelebrityItem() {
    }
}
