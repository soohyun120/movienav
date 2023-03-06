package com.movienav.domain.entity;


import com.movienav.domain.BaseEntity;
import com.movienav.domain.entity.enumPackage.UserRole;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEMBER_ID")
    private Long id;

    @Column(nullable = false, length = 20, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 15)
    private String name;

    @Column(length = 8)
    private String birth;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column(nullable = false, length = 11)
    private String phone;

    @Column(nullable = false, length = 45, unique = true)
    private String email;

    @Builder
    public Member(String username, String password, String name, String birth, String phone, String email) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.birth = birth;
        this.phone = phone;
        this.email = email;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public void update(String phone, String email) {
        this.phone = phone;
        this.email = email;
    }

    public void updatePw(PasswordEncoder passwordEncoder, String newPw) {
        this.password = passwordEncoder.encode(newPw);
    }

    /**
     * 패스워드 암호화
     */
    public void encodePassword(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(password);
    }

    /**
     *패스워드 일치 검증
     */
    public boolean matchPassword(PasswordEncoder passwordEncoder, String checkPw) {
        return passwordEncoder.matches(checkPw, getPassword());
    }
}
