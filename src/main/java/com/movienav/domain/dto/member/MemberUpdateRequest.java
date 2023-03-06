package com.movienav.domain.dto.member;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MemberUpdateRequest {
    private String phone;
    private String email;

}
