package com.nadia.openplatfrom.isv.manage.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class ServiceResponse {
    private String serviceId;
    private List<ApiResponse> apis;
}
