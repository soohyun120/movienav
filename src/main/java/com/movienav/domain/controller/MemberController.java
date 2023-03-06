package com.movienav.domain.controller;

import com.movienav.domain.dto.member.FindPasswordRequest;
import com.movienav.domain.dto.member.*;
import com.movienav.domain.entity.Member;
import com.movienav.security.config.UserDetailsImpl;
import com.movienav.domain.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {

    private final MemberService memberService;
    private final AuthenticationManager authenticationManager;

    /**
     * JWT 를 사용하면 UserDetailsService 를 호출하지 않기 때문에 @AuthenticationPrincipal 사용 불가능
     * 왜냐하면 @AuthenticationPrincipal 은 UserDetailsService 에서 리턴될 때 만들어지기 때문이다.
     */


    /**
     * 회원 가입
     */
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void join(@RequestBody @Valid MemberJoinRequest memberJoinRequest) {
        memberService.join(memberJoinRequest);
    }

    /**
     * 회원정보 수정
     */
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public void update(Authentication authentication,
                       @RequestBody @Valid MemberUpdateRequest memberUpdateRequest) {

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        memberService.update(userDetails.getUsername(), memberUpdateRequest);
    }

    /**
     * 비밀번호 수정
     */
    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    public void updatePw(Authentication authentication,
                         @RequestBody @Valid PasswordUpdateRequest passwordUpdateRequest) {

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        memberService.updatePw(userDetails.getUsername(), passwordUpdateRequest);

        //세션 변경
        //service 에서 트랜잭션 완료 후 controller 에서 실행해야 security session 이 변경된다.
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userDetails.getUsername(),
                        passwordUpdateRequest.getNewPw()));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
    }

    /**
     * 회원 탈퇴
     */
    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void withdraw(Authentication authentication,
                         @RequestBody @Valid MemberWithdrawRequest memberWithdrawRequest) {

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        memberService.withdraw(userDetails.getUsername(), memberWithdrawRequest.getPassword());

        //세션 삭제
        SecurityContextHolder.clearContext();
    }

    /**
     * 아이디 찾기
     */
    @GetMapping("/username")
    public ResponseEntity findUsername(@RequestBody @Valid FindUsernameRequest findUsernameRequest) {
        return ResponseEntity.ok(memberService.findUsername(findUsernameRequest));
    }

    /**
     * 비밀번호 찾기
     * 비밀번호 변경으로 리다이렉트
     */
    @GetMapping("/password")
    @ResponseStatus(HttpStatus.OK)
    public void findPassword(@RequestBody @Valid FindPasswordRequest findPasswordRequest) {
        memberService.findPassword(findPasswordRequest);
    }

    /**
     * 내 정보
     */
    @GetMapping("/info")
    public ResponseEntity<?> getMyInfo(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        Member member = memberService.getMyInfo(userDetails.getUsername());

        return ResponseEntity.ok(MemberInfoResponse.builder()
                .username(member.getUsername())
                .name(member.getName())
                .email(member.getEmail())
                .birth(member.getBirth())
                .phone(member.getPhone()));
    }
}
