package com.donora.dto;

import com.donora.enums.Role;

public class LoginResponse {
    private Long id;
    private String name;
    private String email;
    private Role role;
    private String phone;
    private String token; // to store token


    public LoginResponse(Long id, String name, String email, String phone,Role role,String token) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
        this.phone = phone;
        this.token=token;
    }
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public Role getRole() {
        return role;
    }

    public String getPhone() {
        return phone;
    }
    public String getToken(){
        return token;
    }
}
