package com.nadia.openplatfrom.isv.account.dto.request;

import com.nadia.openplatfrom.isv.account.enums.AppEnum;
import lombok.Data;

import java.util.List;

@Data
public class AppRequest {
    private Long id;
    private String appKey;
    private String name;
    private String signType;
    private String pubKey;
    private AppEnum status;
    private List<Long> businessPackageId;
}
