package com.nadia.openplatfrom.isv.manage.dto.response;

import lombok.Data;

@Data
public class BusinessPackagesAndApisResponse {
    private boolean checked;
    private String name;
    private String version;
    private int order = 0;
    private String uri;
    private String path;
    private String serviceId;
}
