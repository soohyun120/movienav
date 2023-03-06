package com.movienav.domain.service;

import com.movienav.domain.dto.member.FindPasswordRequest;
import com.movienav.domain.dto.member.FindUsernameRequest;
import com.movienav.domain.entity.Member;
import com.movienav.domain.dto.member.MemberJoinRequest;
import com.movienav.domain.dto.member.MemberUpdateRequest;
import com.movienav.domain.dto.member.PasswordUpdateRequest;
import com.movienav.domain.entity.enumPackage.UserRole;
import com.movienav.domain.repository.*;
import com.movienav.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.movienav.exception.error.MemberErrorCode.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final MovieHeartRepository movieHeartRepository;
    private final RatingRepository ratingRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewHeartRepository reviewHeartRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    /**
     * 회원가입
     */
    @Transactional
    public Long join(MemberJoinRequest memberJoinRequest) {
        Member member = memberJoinRequest.toEntity();
        member.encodePassword(passwordEncoder);
        member.setRole(UserRole.USER);

        //회원 중복 검증
        validateDuplicatedMember(member);

        memberRepository.save(member);

        return member.getId();
    }

    private void validateDuplicatedMember(Member member) {
        if (memberRepository.findByUsername(member.getUsername()).isPresent()) {
            throw new CustomException(ALREADY_EXIST_USERNAME);
        }
    }

    /**
     * 회원정보 수정
     */
    @Transactional
    public void update(String username, MemberUpdateRequest memberUpdateRequest) {
        Member member = memberRepository.findByUsername(username).orElseThrow(
                () -> new CustomException(NO_USER));

        member.update(memberUpdateRequest.getPhone(), memberUpdateRequest.getEmail());
    }

    /**
     * 비밀번호 수정
     */
    @Transactional
    public void updatePw(String username, PasswordUpdateRequest passwordUpdateRequest) {
        Member member = memberRepository.findByUsername(username).orElseThrow(
                () -> new CustomException(NO_USER));

        if (!member.matchPassword(passwordEncoder, passwordUpdateRequest.getCheckPw())) {
            throw new CustomException(WRONG_PASSWORD);
        }

        member.updatePw(passwordEncoder, passwordUpdateRequest.getNewPw());
    }

    /**
     * 회원 탈퇴
     */
    @Transactional
    public void withdraw(String username, String checkPw) {
        Member member = memberRepository.findByUsername(username).orElseThrow(
                () -> new CustomException(NO_USER));

        if (!member.matchPassword(passwordEncoder, checkPw)) {
            throw new CustomException(WRONG_PASSWORD);
        }

        memberRepository.delete(member);

        movieHeartRepository.deleteByMember(member);

        ratingRepository.deleteByMember(member);

        reviewRepository.deleteByMember(member);

        reviewHeartRepository.deleteByMember(member);
    }

    /**
     * 아이디 찾기
     */
    public String findUsername(FindUsernameRequest findUsernameRequest) {
        Member member = memberRepository.findByNameAndPhone(
                        findUsernameRequest.getName(), findUsernameRequest.getPhone())
                .orElseThrow(() -> new CustomException(NO_USER));

        return member.getUsername();
    }

    /**
     * 비밀번호 찾기
     */
    public Member findPassword(FindPasswordRequest findPasswordRequest) {
        return memberRepository.findByUsernameAndEmail(
                findPasswordRequest.getUsername(), findPasswordRequest.getEmail())
                .orElseThrow(() -> new CustomException(NO_USER));
    }

    /**
     * 내 정보
     */
    public Member getMyInfo(String username) {
        return memberRepository.findByUsername(username).orElseThrow(
                () -> new CustomException(NO_USER));
    }
}
