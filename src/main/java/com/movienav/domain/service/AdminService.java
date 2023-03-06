package com.movienav.domain.service;

import com.movienav.domain.entity.Member;
import com.movienav.domain.repository.*;
import com.movienav.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.movienav.exception.error.MemberErrorCode.NO_USER;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminService {

    private final MemberRepository memberRepository;
    private final MovieHeartRepository movieHeartRepository;
    private final RatingRepository ratingRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewHeartRepository reviewHeartRepository;

    /**
     * 모든 회원 정보
     */
    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    /**
     * 특정 회원 정보
     */
    public Member findOne(Long id) {
        return memberRepository.findById(id).orElseThrow(
                () -> new CustomException(NO_USER));
    }

    /**
     * 회원 삭제
     */
    @Transactional
    public void delete(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(
                () -> new CustomException(NO_USER));

        memberRepository.delete(member);

        movieHeartRepository.deleteByMember(member);

        ratingRepository.deleteByMember(member);

        reviewRepository.deleteByMember(member);

        reviewHeartRepository.deleteByMember(member);
    }
}
