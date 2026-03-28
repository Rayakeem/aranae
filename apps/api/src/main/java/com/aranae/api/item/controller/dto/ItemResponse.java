package com.aranae.api.item.controller.dto;

import com.aranae.api.item.domain.Item;

import java.time.LocalDateTime;

public record ItemResponse(
        Long id,
        String name,
        String brand,
        String category,
        Integer price,
        String imageKey,
        String purchaseUrl,
        String affiliateUrl,
        String uniqueKey,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static ItemResponse from(Item item) {
        return new ItemResponse(
                item.getId(),
                item.getName(),
                item.getBrand(),
                item.getCategory(),
                item.getPrice(),
                item.getImageKey(),
                item.getPurchaseUrl(),
                item.getAffiliateUrl(),
                item.getUniqueKey(),
                item.getCreatedAt(),
                item.getUpdatedAt()
        );
    }
}
