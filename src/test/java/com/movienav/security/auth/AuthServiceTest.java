package com.movienav.security.auth;

import com.movienav.domain.entity.Member;
import com.movienav.domain.repository.MemberRepository;
import com.movienav.security.auth.dto.LoginRequest;
import com.movienav.security.auth.dto.TokenDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class AuthServiceTest {

    @Autowired AuthService authService;
    @Autowired MemberRepository memberRepository;
    @PersistenceContext EntityManager em;

    /**
     * 로그인
     */
    @Test
    public void 로그인_성공() throws Exception {
        //given
        Member member = Member.builder().username("memberA").password("abcd1234").name("A").birth("111111")
                .phone("11111111111").email("A@aaa.com").build();
        memberRepository.save(member);

        em.flush();

        LoginRequest loginRequest = LoginRequest.builder().username("memberA").password("abcd1234").build();

        //when
        ResponseEntity<TokenDto> loginResponse = authService.login(loginRequest);

        //then
        assertThat(loginResponse.getBody().getAccessToken()).isNotEmpty();
        assertThat(loginResponse.getBody().getRefreshToken()).isNotEmpty();
    }


    //로그인_실패_아이디틀림
    //로그인_실패_비밀번호틀림


    /**
     * 로그아웃
     */
    //로그아웃_성공

    /**
     * reissue
     */
    //reissue_성공

}