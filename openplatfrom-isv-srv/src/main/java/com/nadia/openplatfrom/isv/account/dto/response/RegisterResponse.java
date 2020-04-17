package com.nadia.openplatfrom.isv.account.dto.response;

import lombok.Data;

@Data
public class RegisterResponse {
    private Long id;

    private String name;

    private String password;

    private String mobile;

    private String email;
}
