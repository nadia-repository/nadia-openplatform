package com.nadia.openplatfrom.isv.manage.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ApiInfoRequest {
    private String name;
    @JsonProperty("serviceId")
    private String srvName;
    private String version;

}
