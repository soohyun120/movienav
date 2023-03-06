package com.movienav.domain.repository;

import com.movienav.domain.entity.Member;
import com.movienav.exception.CustomException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static com.movienav.exception.error.MemberErrorCode.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;
    @PersistenceContext EntityManager em;

    @Test
    public void findByUsername() throws Exception {
        //given
        Member member = Member.builder().username("memberA").password("1234").name("A").birth("111111")
                .phone("11111111111").email("A@aaa.com").build();
        memberRepository.save(member);

        em.flush();

        //when
        Member findMember = memberRepository.findByUsername(member.getUsername()).orElseThrow(
                () -> new CustomException(NO_USER));

        //then
        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(findMember).isEqualTo(member);
    }

    /**
     * 회원가입
     */
    @Test
    public void 회원가입_성공() throws Exception {
        //given
        Member member = Member.builder().username("memberA").password("1234").name("A").birth("111111")
                .phone("11111111111").email("A@aaa.com").build();

        //when
        Member savedMember = memberRepository.save(member);

        //then
        Member findMember = memberRepository.findById(member.getId()).orElseThrow(
                () -> new CustomException(NO_USER));

        assertThat(findMember.getId()).isEqualTo(savedMember.getId());
        assertThat(findMember.getUsername()).isEqualTo(savedMember.getUsername());
        assertThat(findMember).isEqualTo(savedMember);
    }

    @Test
    public void 회원가입_실패_입력하지않은_필드() throws Exception {
        //given
        Member member1 = Member.builder().username(null).password("1234").name("A").birth("111111")
                .phone("11111111111").email("A@aaa.com").build();
        Member member2 = Member.builder().username("memberA").password(null).name("A").birth("111111")
                .phone("11111111111").email("A@aaa.com").build();
        Member member3 = Member.builder().username("memberA").password("1234").name("A").birth("111111")
                .phone(null).email("A@aaa.com").build();
        Member member4 = Member.builder().username("memberA").password("1234").name("A").birth("111111")
                .phone("11111111111").email(null).build();

        //then
        assertThrows(Exception.class, () -> memberRepository.save(member1));
        assertThrows(Exception.class, () -> memberRepository.save(member2));
        assertThrows(Exception.class, () -> memberRepository.save(member3));
        assertThrows(Exception.class, () -> memberRepository.save(member4));
    }

    @Test
    public void 회원가입_실패_아이디중복() throws Exception {
        //given
        Member memberA = Member.builder().username("memberA").password("1234").name("A").birth("111111")
                .phone("11111111111").email("A@aaa.com").build();
        Member memberB = Member.builder().username("memberA").password("1234").name("B").birth("222222")
                .phone("22222222222").email("B@bbb.com").build();

        memberRepository.save(memberA);

        //then
        assertThrows(DataIntegrityViolationException.class, () -> memberRepository.save(memberB));
    }

    @Test
    public void 회원가입_실패_이메일중복() throws Exception {
        //given
        Member memberA = Member.builder().username("memberA").password("1234").name("A").birth("111111")
                .phone("11111111111").email("A@aaa.com").build();
        Member memberB = Member.builder().username("memberB").password("1234").name("B").birth("222222")
                .phone("22222222222").email("A@aaa.com").build();

        memberRepository.save(memberA);

        //then
        assertThrows(DataIntegrityViolationException.class, () -> memberRepository.save(memberB));
    }

    /**
     * 회원정보 수정
     */
    @Test
    public void 회원정보수정_성공() throws Exception {
        //given
        Member member = Member.builder().username("memberA").password("1234").name("A").birth("111111")
                .phone("11111111111").email("A@aaa.com").build();
        memberRepository.save(member);

        em.flush();
        em.clear();

        //when
        String updatePhone = "99999999999";
        String updateEmail = "Z@zzz.com";

        Member findMember = memberRepository.findById(member.getId()).orElseThrow(
                () -> new CustomException(NO_USER));

        findMember.update(updatePhone, updateEmail);
        em.flush();

        //then
        Member updateMember = memberRepository.findById(findMember.getId()).orElseThrow(
                () -> new CustomException(NO_USER));

        assertThat(updateMember.getPhone()).isEqualTo(updatePhone);
        assertThat(updateMember.getEmail()).isEqualTo(updateEmail);
        assertThat(updateMember).isEqualTo(findMember);

    }

    @Test
    public void 비밀번호수정_성공() throws Exception {
        //given
        Member member = Member.builder().username("memberA").password("1234").name("A").birth("111111")
                .phone("11111111111").email("A@aaa.com").build();
        memberRepository.save(member);

        em.flush();
        em.clear();

        //when
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        String updatePassword = "5678";

        Member findMember = memberRepository.findById(member.getId()).orElseThrow(
                () -> new CustomException(NO_USER));

        findMember.updatePw(passwordEncoder, updatePassword);
        em.flush();

        //then
        Member updateMember = memberRepository.findById(findMember.getId()).orElseThrow(
                () -> new CustomException(NO_USER));

        assertThat(passwordEncoder.matches(updatePassword, updateMember.getPassword())).isTrue();
        assertThat(updateMember).isEqualTo(findMember);
    }

    /**
     * 회원 삭제
     */
    @Test
    public void 회원삭제_성공() throws Exception {
        //given
        Member member = Member.builder().username("memberA").password("1234").name("A").birth("111111")
                .phone("11111111111").email("A@aaa.com").build();
        memberRepository.save(member);

        em.flush();
        em.clear();

        //when
        memberRepository.delete(member);

        em.flush();
        em.clear();

        //then
        assertThrows(Exception.class,
                () -> memberRepository.findById(member.getId())
                .orElseThrow(() -> new CustomException(NO_USER)));
    }

    @Test
    public void findByNameAndPhone() throws Exception {
        //given
        Member member = Member.builder().username("memberA").password("1234").name("A").birth("111111")
                .phone("11111111111").email("A@aaa.com").build();
        memberRepository.save(member);

        em.flush();
        em.clear();

        //when
        Member findMember = memberRepository.findByNameAndPhone(member.getName(), member.getPhone()).orElseThrow(
                () -> new CustomException(NO_USER));

        //then
       assertThat(findMember.getName()).isEqualTo(member.getName());
       assertThat(findMember.getPhone()).isEqualTo(member.getPhone());
       assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
    }

    @Test
    public void findByUsernameAndEmail() throws Exception {
        //given
        Member member = Member.builder().username("memberA").password("1234").name("A").birth("111111")
                .phone("11111111111").email("A@aaa.com").build();
        memberRepository.save(member);

        em.flush();
        em.clear();

        //when
        Member findMember = memberRepository.findByUsernameAndEmail(member.getUsername(), member.getEmail()).orElseThrow(
                () -> new CustomException(NO_USER));

        //then
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(findMember.getEmail()).isEqualTo(member.getEmail());
    }

}