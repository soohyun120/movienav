package com.movienav.domain.dto.admin;

import com.movienav.domain.entity.enumPackage.UserRole;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AdminResponse {
    private Long id;
    private String username;
    private String password;
    private String name;
    private String birth;
    private UserRole role;
    private String phone;
    private String email;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;

    public AdminResponse(Long id, String username, String password, String name, String birth, UserRole role,
                         String phone, String email, LocalDateTime createdDate, LocalDateTime lastModifiedDate) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.birth = birth;
        this.role = role;
        this.phone = phone;
        this.email = email;
        this.createdDate = createdDate;
        this.lastModifiedDate = lastModifiedDate;
    }
}
