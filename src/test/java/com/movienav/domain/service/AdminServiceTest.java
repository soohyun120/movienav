package com.movienav.domain.service;

import com.movienav.domain.dto.member.MemberJoinRequest;
import com.movienav.domain.entity.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class AdminServiceTest {

    @Autowired AdminService adminService;
    @Autowired MemberService memberService;
    @PersistenceContext EntityManager em;

    private MemberJoinRequest createMemberJoinRequest() {
        return MemberJoinRequest.builder().username("memberA").password("1234").name("A").birth("111111")
                .phone("11111111111").email("A@aaa.com").build();
    }

    @Test
    public void 모든_회원정보_찾기() throws Exception {
        //given
        MemberJoinRequest memberJoinRequest = createMemberJoinRequest();
        memberService.join(memberJoinRequest);

        MemberJoinRequest memberJoinRequest2 =
                MemberJoinRequest.builder().username("memberB").password("1234").name("B").birth("111111")
                        .phone("22222222222").email("B@bbb.com").build();
        memberService.join(memberJoinRequest2);

        em.flush();
        em.clear();

        //when
        List<Member> all = adminService.findAll();

        //then
        assertThat(all.size()).isEqualTo(2);
        assertThat(all.get(0).getUsername()).isEqualTo(memberJoinRequest.getUsername());
        assertThat(all.get(1).getUsername()).isEqualTo(memberJoinRequest2.getUsername());
    }

    @Test
    public void 특정_회원정보_찾기() throws Exception {
        //given
        MemberJoinRequest memberJoinRequest = createMemberJoinRequest();
        Long savedId = memberService.join(memberJoinRequest);

        em.flush();
        em.clear();

        //when
        Member findMember = adminService.findOne(savedId);

        //then
        assertThat(findMember.getId()).isEqualTo(savedId);
    }

    @Test
    public void 회원_삭제() throws Exception {
        //given

        //when

        //then

    }
}
