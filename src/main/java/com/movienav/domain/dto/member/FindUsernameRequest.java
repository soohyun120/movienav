package com.movienav.domain.dto.member;

import lombok.Data;

@Data
public class FindUsernameRequest {
    private String name;
    private String phone;
}
