package com.nadia.openplatfrom.isv.account.dto.request;

import lombok.Data;

@Data
public class RegisterRequest {
    private String name;
    private String description;
    private String password;
    private String mobile;
    private String mail;
}
