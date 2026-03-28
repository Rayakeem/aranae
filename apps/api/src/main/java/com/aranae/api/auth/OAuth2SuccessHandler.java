package com.aranae.api.auth;

import com.aranae.api.user.domain.User;
import com.aranae.api.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;

    public OAuth2SuccessHandler(JwtProvider jwtProvider, UserRepository userRepository) {
        this.jwtProvider = jwtProvider;
        this.userRepository = userRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("유저를 찾을 수 없습니다."));

        String token = jwtProvider.generateToken(user.getId());

        // JWT를 HttpOnly 쿠키에 담아서 내려줌 (URL에 토큰 노출 방지)
        jakarta.servlet.http.Cookie cookie = new jakarta.servlet.http.Cookie("token", token);
        cookie.setHttpOnly(true);          // JS에서 접근 불가 (XSS 방어)
        cookie.setSecure(false);           // TODO: 배포 시 true로 변경 (HTTPS 전용)
        cookie.setPath("/");               // 모든 경로에서 쿠키 전송
        cookie.setMaxAge(60 * 60 * 24);    // 24시간
        response.addCookie(cookie);

        // 토큰 없이 프론트로 리다이렉트
        getRedirectStrategy().sendRedirect(request, response, "http://localhost:3000");
    }
}
