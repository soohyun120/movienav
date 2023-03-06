package com.movienav.domain.controller;

import com.movienav.domain.dto.admin.AdminResponse;
import com.movienav.domain.dto.review.ReviewResponse;
import com.movienav.domain.entity.Member;
import com.movienav.domain.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    /**
     * 모든 회원 정보
     */
    @GetMapping
    public ResponseEntity findMembers() {
        List<Member> members = adminService.findAll();
        List<ReviewResponse> collect = (List) members.stream().map((m) -> {
            return new AdminResponse(m.getId(), m.getUsername(), m.getPassword(), m.getName(), m.getBirth(),
                    m.getRole(), m.getPhone(), m.getEmail(), m.getCreatedDate(), m.getLastModifiedDate());
        }).collect(Collectors.toList());

        return ResponseEntity.ok(collect);
    }

    /**
     * 특정 회원 정보
     */
    @GetMapping("/{memberId}")
    public ResponseEntity findMember(@PathVariable("memberId") Long memberId) {
        Member member = adminService.findOne(memberId);
        return ResponseEntity.ok(
                new AdminResponse(member.getId(), member.getUsername(), member.getPassword(),
                        member.getName(), member.getBirth(), member.getRole(), member.getPhone(),
                        member.getEmail(), member.getCreatedDate(), member.getLastModifiedDate()));
    }

    /**
     * 회원 삭제
     */
    @DeleteMapping("/{memberId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteMember(@PathVariable("memberId") Long memberId) {
        adminService.delete(memberId);
    }
}
