package com.nadia.openplatfrom.isv.account.domain;

import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
public class AppRoutePermission {
    private String appKey;
    private List<String> routeIdList = Collections.emptyList();
    private String routeIdListMd5;

}
