package com.movienav.domain.dto.member;

import lombok.Data;

@Data
public class FindPasswordRequest {
    private String username;
    private String email;
}
