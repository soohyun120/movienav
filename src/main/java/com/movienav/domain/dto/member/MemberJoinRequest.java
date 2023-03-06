package com.movienav.domain.dto.member;

import com.movienav.domain.entity.Member;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MemberJoinRequest {

    @NotBlank(message = "아이디를 입력해주세요.")
    @Size(min = 1, max = 20, message = "아이디는 20자리 내외로 입력해주세요.")
    private String username;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Pattern(regexp = "[a-zA-Z1-9]{8,20}",
        message = "비밀번호는 영어와 숫자 포함해서 8~20자리 이내로 입력해주세요.")
    private String password;

    @NotBlank(message = "이름을 입력해주세요.")
    private String name;

    private String birth;

    @NotBlank(message = "전화번호를 입력해주세요.")
    private String phone;

    @NotBlank(message = "이메일을 입력해주세요.")
    @Email(message = "이메일 형식을 맞춰주세요.")
    private String email;

    public Member toEntity() {
        return Member.builder()
                .username(username)
                .password(password)
                .name(name)
                .birth(birth)
                .phone(phone)
                .email(email)
                .build();
    }
}