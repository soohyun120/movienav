package com.movienav.domain.repository;

import com.movienav.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUsername(String username);
    Optional<Member> findByNameAndPhone(String name, String phone);
    Optional<Member> findByUsernameAndEmail(String username, String email);
}
