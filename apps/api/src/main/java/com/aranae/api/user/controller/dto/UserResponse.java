package com.aranae.api.user.controller.dto;

import com.aranae.api.user.domain.User;
import com.aranae.api.user.domain.UserRole;

public record UserResponse(
        Long id,
        String email,
        String nickname,
        String profileImageUrl,
        UserRole role
) {
    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getNickname(),
                user.getProfileImageUrl(),
                user.getRole()
        );
    }
}
