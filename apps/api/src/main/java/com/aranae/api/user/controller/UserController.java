package com.aranae.api.user.controller;

import com.aranae.api.user.controller.dto.UserResponse;
import com.aranae.api.user.domain.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    // 현재 로그인한 유저 정보 반환
    // JwtAuthenticationFilter가 이미 User 객체를 SecurityContext에 넣어줬으므로
    // DB 조회 없이 파라미터로 바로 받을 수 있다
    @GetMapping("/me")
    public UserResponse getMe(@AuthenticationPrincipal User user) {
        return UserResponse.from(user);
    }
}
