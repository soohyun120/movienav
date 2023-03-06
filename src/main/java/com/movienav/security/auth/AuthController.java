package com.movienav.security.auth;

import com.movienav.security.auth.dto.LoginRequest;
import com.movienav.security.auth.dto.TokenDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody @Valid LoginRequest loginRequest) {
       return authService.login(loginRequest);
    }

    @PostMapping("/logout")
    public ResponseEntity<Object> logout(@RequestBody @Valid TokenDto tokenDto) {
        ResponseEntity<Object> logout = authService.logout(tokenDto);

        //세션 삭제
        SecurityContextHolder.clearContext();

        return logout;
    }

    @PostMapping("/reissue")
    public ResponseEntity<TokenDto> reissue(@RequestBody @Valid TokenDto tokenDto) {
        return authService.reissue(tokenDto.getRefreshToken());
    }
}
