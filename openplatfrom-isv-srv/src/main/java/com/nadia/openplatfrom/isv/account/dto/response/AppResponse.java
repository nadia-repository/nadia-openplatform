package com.nadia.openplatfrom.isv.account.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class AppResponse {
    private Long id;

    private Long isvId;

    private String name;

    private String appKey;

    private Long serviceId;

    private String status;

    private String pubKey;

    private String signType;

    private List<Long> businessPackageId;
}
