package com.nadia.openplatfrom.isv.manage.service.impl;

import com.alibaba.fastjson.JSON;
import com.nadia.openplatfrom.isv.account.domain.AppBusinessPackage;
import com.nadia.openplatfrom.isv.account.domain.AppInfo;
import com.nadia.openplatfrom.isv.account.dto.response.ApiResponse;
import com.nadia.openplatfrom.isv.account.service.AppBusinessPackageService;
import com.nadia.openplatfrom.isv.account.service.AppInfoService;
import com.nadia.openplatfrom.isv.manage.domain.ApiInfo;
import com.nadia.openplatfrom.isv.manage.dto.response.AppApiResponse;
import com.nadia.openplatfrom.isv.manage.dto.response.ServiceResponse;
import com.nadia.openplatfrom.isv.manage.service.ApiInfoService;
import com.nadia.openplatfrom.isv.manage.service.ApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ApiServiceImpl implements ApiService {

    @Resource
    private ZookeeperTool zookeeperTool;
    @Resource
    private AppInfoService appInfoService;
    @Resource
    private AppBusinessPackageService appBusinessPackageService;
    @Resource
    private ApiInfoService apiInfoService;

    @Override
    public List<ServiceResponse> getServices() throws Exception {
        String routeRootPath = Constants.ServiceConstants.SERVICE_ROUTE_PATH;
        List<ChildData> childrenData = zookeeperTool.getChildrenData(routeRootPath);
        List<ServiceResponse> response = childrenData.stream().filter(childData -> childData.getData() != null && childData.getData().length > 0)
                .map(childData -> {
                    String serviceNodeData = new String(childData.getData());
                    ServiceResponse serviceResponse = JSON.parseObject(serviceNodeData, ServiceResponse.class);
                    return serviceResponse;
                }).collect(Collectors.toList());
        return response;
    }

    @Override
    public List<ApiResponse> getApisByServiceId(String serviceId) throws Exception {
        String servicePath = Constants.ServiceConstants.SERVICE_ROUTE_PATH + "/" + serviceId;
        List<ChildData> childrenData = zookeeperTool.getChildrenData(servicePath);
        List<ApiResponse> response = childrenData.stream().filter(childData -> childData.getData() != null && childData.getData().length > 0)
                .map(childData -> {
                    String apisNodeData = new String(childData.getData());
                    ApiResponse apiResponse = JSON.parseObject(apisNodeData, ApiResponse.class);
                    apiResponse.setServiceId(serviceId);
                    return apiResponse;
                }).filter(childData -> childData.getOrder() != Integer.MIN_VALUE).collect(Collectors.toList());
        return response;
    }

    @Override
    public List<ApiResponse> getApis() throws Exception {
        String routeRootPath = Constants.ServiceConstants.SERVICE_ROUTE_PATH;
        List<ApiResponse> response = new LinkedList<>();;
        List<ChildData> childrenData = zookeeperTool.getChildrenData(routeRootPath);
        childrenData.forEach(childData -> {
            String serviceNodeData = new String(childData.getData());
            ServiceResponse serviceResponse = JSON.parseObject(serviceNodeData, ServiceResponse.class);
            try {
                List<ApiResponse> apis = this.getApisByServiceId(serviceResponse.getServiceId());
                response.addAll(apis);
            } catch (Exception e) {
                log.error("getServicesAndApis error:{}",e);
            }
        });
        return response;

    }

    @Override
    public List<ServiceResponse> getServicesAndApis() throws Exception {
        String routeRootPath = Constants.ServiceConstants.SERVICE_ROUTE_PATH;
        List<ServiceResponse> response = new LinkedList<>();
        List<ChildData> childrenData = zookeeperTool.getChildrenData(routeRootPath);
        childrenData.forEach(childData -> {
            String serviceNodeData = new String(childData.getData());
            ServiceResponse serviceResponse = JSON.parseObject(serviceNodeData, ServiceResponse.class);
            List<ApiResponse> apis = null;
            try {
                apis = this.getApisByServiceId(serviceResponse.getServiceId());
            } catch (Exception e) {
                log.error("getServicesAndApis error:{}",e);
            }
            serviceResponse.setApis(apis);
            response.add(serviceResponse);
        });
        return response;
    }

    @Override
    public List<AppApiResponse> getAppsAndApis() throws Exception {
        List<AppApiResponse> response = new ArrayList<>();
        List<AppInfo> apps = appInfoService.getAllApps();

        apps.forEach(app -> {
            List<AppBusinessPackage> appBusinessPackages = appBusinessPackageService.getAppBusinessPackageByAppId(app.getId());

            appBusinessPackages.forEach(appBusinessPackage -> {
                List<ApiInfo> apis = apiInfoService.getApiInfosByBusinessPackageId(appBusinessPackage.getId());
                AppApiResponse appApiResponse = new AppApiResponse();
                appApiResponse.setAppKey(app.getAppKey());
                appApiResponse.setApis(apis);
                response.add(appApiResponse);
            });
        });
        return response;
    }
}
