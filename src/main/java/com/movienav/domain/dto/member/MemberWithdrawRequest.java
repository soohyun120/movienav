package com.movienav.domain.dto.member;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MemberWithdrawRequest {
    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;
}
