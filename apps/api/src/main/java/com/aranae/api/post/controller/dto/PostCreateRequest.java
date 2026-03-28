package com.aranae.api.post.controller.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record PostCreateRequest(
        @NotBlank String title,
        @Valid @NotNull PostCelebrityRequest celebrity,
        String celebrityImageKey,
        String sourceChannel,
        String sourceUrl,
        @Size(max = 3) List<String> tags,
        @Valid @NotEmpty List<PostItemRequest> items
) {}
