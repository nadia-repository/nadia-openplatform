package com.nadia.openplatfrom.isv.manage.service;


import com.nadia.openplatfrom.isv.account.dto.request.AppRequest;
import com.nadia.openplatfrom.isv.account.dto.response.ApiResponse;
import com.nadia.openplatfrom.isv.account.dto.response.BusinessBackageApiResponse;
import com.nadia.openplatfrom.isv.manage.domain.ApiInfo;
import com.nadia.openplatfrom.isv.manage.dto.request.ApiInfoRequest;

import java.util.List;

public interface ApiInfoService {

    List<ApiInfo> getApiInfosByBusinessPackageIds(List<Long> ids);

    List<ApiInfo> getApiInfosByBusinessPackageId(Long id);

    ApiResponse getApiInfoByApp(Long isvId, AppRequest appRequest);

    BusinessBackageApiResponse getApiInfos();

    void addApiInfos(Long isvId, List<ApiInfoRequest> apiInfos);

    int deleteApisByBusinessPackageId(Long businessPackageId);

    void apiInfo(String name);
}
