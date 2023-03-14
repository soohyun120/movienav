package com.movienav.domain.service;

import com.movienav.domain.dto.member.FindPasswordRequest;
import com.movienav.domain.dto.member.FindUsernameRequest;
import com.movienav.domain.entity.*;
import com.movienav.domain.dto.member.MemberJoinRequest;
import com.movienav.domain.dto.member.MemberUpdateRequest;
import com.movienav.domain.dto.member.PasswordUpdateRequest;
import com.movienav.domain.entity.enumPackage.UserRole;
import com.movienav.domain.repository.*;
import com.movienav.exception.CustomException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.movienav.exception.error.MemberErrorCode.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;
    @Autowired MovieHeartRepository movieHeartRepository;
    @Autowired ReviewHeartRepository reviewHeartRepository;
    @Autowired ReviewRepository reviewRepository;
    @Autowired RatingRepository ratingRepository;
    @PersistenceContext EntityManager em;

    private MemberJoinRequest createMemberJoinRequest() {
        return MemberJoinRequest.builder().username("memberA").password("1234").name("A").birth("111111")
                .phone("11111111111").email("A@aaa.com").build();
    }

    /**
     * 회원가입
     */
    @Test
    public void 회원가입_성공() throws Exception {
        //given
        MemberJoinRequest memberJoinRequest = createMemberJoinRequest();

        //when
        Long savedId = memberService.join(memberJoinRequest);

        em.flush();

        //then
        Member findMember = memberRepository.findByUsername(memberJoinRequest.getUsername()).orElseThrow(
                () -> new CustomException(NO_USER));

        assertThat(findMember.getId()).isEqualTo(savedId);
        assertThat(findMember.getUsername()).isEqualTo(memberJoinRequest.getUsername());
        assertThat(findMember.getRole()).isEqualTo(UserRole.USER);
    }

    @Test
    public void 회원가입_실패_아이디중복() throws Exception {
        //given
        MemberJoinRequest memberJoinRequestA = createMemberJoinRequest();
        memberService.join(memberJoinRequestA);

        em.flush();
        em.clear();

        //when
        MemberJoinRequest memberJoinRequestB = createMemberJoinRequest();

        //then
        assertThrows(CustomException.class, () -> memberService.join(memberJoinRequestB))
                .getErrorCode().equals(ALREADY_EXIST_USERNAME);
    }

    @Test
    public void 회원가입_실패_입력하지않은_필드() throws Exception {
        //given
        MemberJoinRequest memberJoinRequest1 =
                MemberJoinRequest.builder().username(null).password("1234").name("A").birth("111111")
                .phone("11111111111").email("A@aaa.com").build();
        MemberJoinRequest memberJoinRequest2 =
                MemberJoinRequest.builder().username("memberA").password(null).name("A").birth("111111")
                        .phone("11111111111").email("A@aaa.com").build();
        MemberJoinRequest memberJoinRequest3 =
                MemberJoinRequest.builder().username("memberA").password("1234").name(null).birth("111111")
                        .phone("11111111111").email("A@aaa.com").build();
        MemberJoinRequest memberJoinRequest4 =
                MemberJoinRequest.builder().username("memberA").password("1234").name("A").birth("111111")
                        .phone(null).email("A@aaa.com").build();
        MemberJoinRequest memberJoinRequest5 =
                MemberJoinRequest.builder().username("memberA").password("1234").name("A").birth("111111")
                        .phone("11111111111").email(null).build();

        //when, then
        assertThrows(Exception.class, () -> memberService.join(memberJoinRequest1));
        assertThrows(Exception.class, () -> memberService.join(memberJoinRequest2));
        assertThrows(Exception.class, () -> memberService.join(memberJoinRequest3));
        assertThrows(Exception.class, () -> memberService.join(memberJoinRequest4));
        assertThrows(Exception.class, () -> memberService.join(memberJoinRequest5));
    }

    /**
     * 회원정보 수정
     */
    @Test
    public void 회원정보수정_성공() throws Exception {
        //given
        MemberJoinRequest memberJoinRequest = createMemberJoinRequest();
        String username = memberJoinRequest.getUsername();

        memberService.join(memberJoinRequest);

        em.flush();
        em.clear();

        //when
        MemberUpdateRequest memberUpdateRequest =
                MemberUpdateRequest.builder().phone("99999999999").email("Z@zzz.com").build();

        memberService.update(username, memberUpdateRequest);

        //then
        Member findMember = memberRepository.findByUsername(username).orElseThrow(
                () -> new CustomException(NO_USER));

        assertThat(findMember.getPhone()).isEqualTo(memberUpdateRequest.getPhone());
        assertThat(findMember.getEmail()).isEqualTo(memberUpdateRequest.getEmail());
    }

    @Test
    public void 비밀번호수정_성공() throws Exception {
        //given
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        MemberJoinRequest memberJoinRequest = createMemberJoinRequest();
        String username = memberJoinRequest.getUsername();
        memberService.join(memberJoinRequest);

        em.flush();
        em.clear();

        String checkPassword = memberJoinRequest.getPassword();
        String newPassword = "5678";
        PasswordUpdateRequest updateRequest =
                PasswordUpdateRequest.builder().checkPw(checkPassword).newPw(newPassword).build();

        //when
        memberService.updatePw(username, updateRequest);

        //then
        Member findMember = memberRepository.findByUsername(username).orElseThrow(
                () -> new CustomException(NO_USER));

        assertThat(findMember.matchPassword(passwordEncoder, newPassword)).isTrue();
    }

    @Test
    public void 비밀번호수정_실패_비밀번호_불일치() throws Exception {
        //given
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        MemberJoinRequest memberJoinRequest = createMemberJoinRequest();
        String username = memberJoinRequest.getUsername();
        memberService.join(memberJoinRequest);

        em.flush();
        em.clear();

        String checkPassword = "abcd";
        String newPassword = "5678";
        PasswordUpdateRequest updateRequest =
                PasswordUpdateRequest.builder().checkPw(checkPassword).newPw(newPassword).build();

        //when, then
        assertThrows(CustomException.class, () -> memberService.updatePw(username, updateRequest))
                .getErrorCode().equals(WRONG_PASSWORD);
    }

    /**
     * 회원탈퇴
     */
    @Test
    public void 회원탈퇴_성공() throws Exception {
        //given
        MemberJoinRequest memberJoinRequest = createMemberJoinRequest();
        String username = memberJoinRequest.getUsername();
        String checkPassword = memberJoinRequest.getPassword();

        memberService.join(memberJoinRequest);

        Member member = memberRepository.findByUsername(username).orElseThrow(
                () -> new CustomException(NO_USER));
        Review review = new Review(member, null, "zzzz");
        reviewRepository.save(review);
        reviewHeartRepository.save(new ReviewHeart(member, review));
        movieHeartRepository.save(new MovieHeart(member, null));
        ratingRepository.save(new Rating(member, null, null));

        em.flush();
        em.clear();

        //when
        memberService.withdraw(username, checkPassword);

        //then
        assertThrows(CustomException.class, () -> memberRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(NO_USER)))
                .getErrorCode().equals(NO_USER);
        assertThat(reviewHeartRepository.findByMember(member).size()).isEqualTo(0);
        assertThat(reviewRepository.findByMember(member).size()).isEqualTo(0);
        assertThat(movieHeartRepository.findByMember(member).size()).isEqualTo(0);
        assertThat(ratingRepository.findByMember(member).size()).isEqualTo(0);
    }

    @Test
    public void 회원탈퇴_실패_비밀번호_불일치() throws Exception {
        //given
        MemberJoinRequest memberJoinRequest = createMemberJoinRequest();
        String username = memberJoinRequest.getUsername();

        memberService.join(memberJoinRequest);

        em.flush();
        em.clear();

        //when, then
        assertThrows(CustomException.class, () -> memberService.withdraw(username, "abcd"))
                .getErrorCode().equals(WRONG_PASSWORD);
    }

    @Test
    public void 아이디_찾기_성공() throws Exception {
        //given
        MemberJoinRequest memberJoinRequest = createMemberJoinRequest();
        Long savedId = memberService.join(memberJoinRequest);

        em.flush();
        em.clear();

        FindUsernameRequest findUsernameRequest = new FindUsernameRequest();
        findUsernameRequest.setName("A");
        findUsernameRequest.setPhone("11111111111");

        //when
        String findUsername = memberService.findUsername(findUsernameRequest);

        //then
        Member savedMember = memberRepository.findById(savedId).orElseThrow(
                () -> new CustomException((NO_USER)));
       assertThat(findUsername).isEqualTo(savedMember.getUsername());
    }

    @Test
    public void 아이디_찾기_실패() throws Exception {
        //given
        MemberJoinRequest memberJoinRequest = createMemberJoinRequest();
        memberService.join(memberJoinRequest);

        em.flush();
        em.clear();

        FindUsernameRequest findUsernameRequest = new FindUsernameRequest();
        findUsernameRequest.setName("B");
        findUsernameRequest.setPhone("11111111111");

        //when, then
        assertThrows(CustomException.class, () -> memberService.findUsername(findUsernameRequest))
                .getErrorCode().equals(NO_USER);
    }

    @Test
    public void 비밀번호_찾기_성공() throws Exception {
        //given
        MemberJoinRequest memberJoinRequest = createMemberJoinRequest();
        Long savedId = memberService.join(memberJoinRequest);

        em.flush();
        em.clear();

        FindPasswordRequest findPasswordRequest = new FindPasswordRequest();
        findPasswordRequest.setUsername("memberA");
        findPasswordRequest.setEmail("A@aaa.com");

        //when
        Member findMember = memberService.findPassword(findPasswordRequest);

        //then
        Member savedMember = memberRepository.findById(savedId).orElseThrow(
                () -> new CustomException((NO_USER)));
        assertThat(findMember.getPassword()).isEqualTo(savedMember.getPassword());
    }

    @Test
    public void 비밀번호_찾기_실패() throws Exception {
        //given
        MemberJoinRequest memberJoinRequest = createMemberJoinRequest();
        Long savedId = memberService.join(memberJoinRequest);

        em.flush();
        em.clear();

        FindPasswordRequest findPasswordRequest = new FindPasswordRequest();
        findPasswordRequest.setUsername("memberB");
        findPasswordRequest.setEmail("B@bbb.com");

        //when, then
        assertThrows(CustomException.class, () -> memberService.findPassword(findPasswordRequest))
                .getErrorCode().equals(NO_USER);
    }

    @Test
    public void 내정보_찾기_성공() throws Exception {
        //given
        MemberJoinRequest memberJoinRequest = createMemberJoinRequest();
        memberService.join(memberJoinRequest);

        em.flush();
        em.clear();

        //when
        Member findMember = memberService.getMyInfo(memberJoinRequest.getUsername());

        //then
        assertThat(findMember.getUsername()).isEqualTo(memberJoinRequest.getUsername());
        assertThat(findMember.getEmail()).isEqualTo(memberJoinRequest.getEmail());
    }

    @Test
    public void 내정보_찾기_실패() throws Exception {
        //given
        MemberJoinRequest memberJoinRequest = createMemberJoinRequest();
        memberService.join(memberJoinRequest);

        em.flush();
        em.clear();

        //when, then
        assertThrows(CustomException.class, () -> memberService.getMyInfo("memberB"))
                .getErrorCode().equals(NO_USER);
    }
}