package com.nadia.openplatfrom.isv.manage.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class BusinessPackageRequest {
    private String name;
    private Long id;
    private List<ApiInfoRequest> apiInfos;
}
