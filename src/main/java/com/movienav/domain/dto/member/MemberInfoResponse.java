package com.movienav.domain.dto.member;

import lombok.Builder;
import lombok.Data;

@Data
public class MemberInfoResponse {
    private String username;
    private String name;
    private String email;
    private String birth;
    private String phone;

    @Builder
    public MemberInfoResponse(String username, String name, String email, String birth, String phone) {
        this.username = username;
        this.name = name;
        this.email = email;
        this.birth = birth;
        this.phone = phone;
    }
}
