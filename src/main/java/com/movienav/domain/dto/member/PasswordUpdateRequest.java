package com.movienav.domain.dto.member;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PasswordUpdateRequest {

    @NotBlank(message = "현재 비밀번호를 입력해주세요.")
    private String checkPw;

    @NotBlank(message = "새로운 비밀번호를 입력해주세요.")
    @Pattern(regexp = "[a-zA-Z1-9]{8,20}",
            message = "비밀번호는 영어와 숫자 포함해서 8~20자리 이내로 입력해주세요.")
    private String newPw;
}
