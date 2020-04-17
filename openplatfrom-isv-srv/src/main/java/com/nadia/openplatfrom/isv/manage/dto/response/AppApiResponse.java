package com.nadia.openplatfrom.isv.manage.dto.response;

import com.nadia.openplatfrom.isv.manage.domain.ApiInfo;
import lombok.Data;

import java.util.List;

@Data
public class AppApiResponse {
    private String appKey;
    private List<ApiInfo> apis;
}
