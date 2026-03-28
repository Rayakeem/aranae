package com.aranae.api.item.controller.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record ItemCreateRequest(
        @NotBlank String name,
        String brand,
        String category,
        @Min(0) Integer price,
        String imageKey,
        String purchaseUrl,
        String affiliateUrl
) {}
