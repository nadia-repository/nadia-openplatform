package com.nadia.openplatfrom.isv.account.dto.response;

import lombok.Data;

@Data
public class LoginResponse {
    private Long id;
    private String name;
    private String accessToken;
    private String refreshToken;
}
