package com.nadia.openplatfrom.isv.account.dto.request;

import lombok.Data;

@Data
public class LoginRequest {
    private String name;
    private String password;
}
