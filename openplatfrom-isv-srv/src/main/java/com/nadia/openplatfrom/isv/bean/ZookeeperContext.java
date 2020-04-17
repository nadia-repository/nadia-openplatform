package com.nadia.openplatfrom.isv.bean;

import lombok.extern.slf4j.Slf4j;

import static com.nadia.openplatfrom.isv.bean.SopConstants.*;


@Slf4j
public class ZookeeperContext {


    public static String getRouteRootPath() {
        return SOP_SERVICE_ROUTE_PATH;
    }

    public static String getRoutePermissionPath() {
        return SOP_ROUTE_PERMISSION_PATH;
    }

    public static String getAppInfoChannelPath() {
        return SOP_MSG_CHANNEL_PATH + "/appinfo";
    }

    public static String getIsvRoutePermissionChannelPath() {
        return SOP_MSG_CHANNEL_PATH + "/app-route-permission";
    }

    public static String getRouteConfigChannelPath() {
        return SOP_MSG_CHANNEL_PATH + "/route-conf";
    }

    public static String getLimitConfigChannelPath() {
        return SOP_MSG_CHANNEL_PATH + "/limit-conf";
    }

}
