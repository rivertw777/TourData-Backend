package DNA_Backend.api_server.domain.auth.controller;

import DNA_Backend.api_server.domain.auth.dto.request.UpdateUsernameRequest;
import DNA_Backend.api_server.domain.auth.dto.response.CheckFirstSocialLoginResponse;
import DNA_Backend.api_server.domain.auth.service.AuthService;
import DNA_Backend.api_server.global.security.cookie.CookieManager;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final CookieManager cookieManager;

    @Operation(summary = "USER - 소셜 계정 최초 로그인 확인")
    @GetMapping("/first-social-login")
    public ResponseEntity<CheckFirstSocialLoginResponse> checkFirstSocialLogin(@AuthenticationPrincipal(expression = "username") String username) {
        CheckFirstSocialLoginResponse response = authService.checkFirstSocialLogin(username);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "USER - 새 이름 입력")
    @PatchMapping("/name")
    public ResponseEntity<Void> updateUsername(HttpServletRequest request, HttpServletResponse response,
                                            @AuthenticationPrincipal(expression = "username") String username,
                                            @Valid @RequestBody UpdateUsernameRequest requestParam) {
        cookieManager.deleteCookie(request, response);
        authService.updateUsername(username, requestParam.newUsername());
        String token = authService.getToken(requestParam.newUsername());
        cookieManager.setCookie(response, token);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "USER - 로그아웃")
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        cookieManager.deleteCookie(request, response);
        return ResponseEntity.ok().build();
    }

}
