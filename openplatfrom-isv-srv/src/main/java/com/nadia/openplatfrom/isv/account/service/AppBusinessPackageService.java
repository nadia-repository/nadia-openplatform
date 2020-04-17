package com.nadia.openplatfrom.isv.account.service;


import com.nadia.openplatfrom.isv.account.domain.AppBusinessPackage;

import java.util.List;

public interface AppBusinessPackageService {
    List<AppBusinessPackage> getAppBusinessPackageByAppId(Long appId);

    int save(AppBusinessPackage record);

    void deleteByAppId(Long appId);
}
