package com.aranae.api.post.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public record PostUpdateRequest(
        @NotBlank String title,
        String sourceChannel,
        String sourceUrl,
        @Size(max = 3) List<String> tags
) {}
