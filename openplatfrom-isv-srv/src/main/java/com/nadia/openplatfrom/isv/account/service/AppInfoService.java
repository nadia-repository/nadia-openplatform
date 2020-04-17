package com.nadia.openplatfrom.isv.account.service;


import com.nadia.openplatfrom.isv.account.domain.AppInfo;
import com.nadia.openplatfrom.isv.account.dto.request.AppRequest;
import com.nadia.openplatfrom.isv.account.dto.response.AppResponse;

import java.util.List;

public interface AppInfoService {
    AppResponse addApp(Long isvId, AppRequest appRequest);

    AppResponse modifyApp(AppRequest appRequest);

    List<AppInfo> getApps(Long isvId, AppRequest appRequest);

    List<AppInfo> getAllApps();

    AppResponse getAppById(Long isvId, Long id);

}
