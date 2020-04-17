package com.nadia.openplatfrom.isv.manage.controller;

import com.nadia.openplatform.common.rest.RestBody;
import com.nadia.openplatfrom.isv.manage.dto.response.ApiResponse;
import com.nadia.openplatfrom.isv.manage.dto.response.AppApiResponse;
import com.nadia.openplatfrom.isv.manage.dto.response.ServiceResponse;
import com.nadia.openplatfrom.isv.manage.service.ApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/manage")
public class ApiController {

    @Resource
    private ApiService apiService;

    @RequestMapping(value = "/services", method = RequestMethod.GET)
    public RestBody<List<ServiceResponse>> getServices() throws Exception {
        RestBody<List<ServiceResponse>> response = new RestBody<>();
        List<ServiceResponse> services = apiService.getServices();
        response.setData(services);
        return response;
    }

    @RequestMapping(value = "/service/{serviceId}", method = RequestMethod.GET)
    public RestBody<List<ApiResponse>> getApis(@PathVariable("serviceId") String serviceId) throws Exception {
        RestBody<List<ApiResponse>> response = new RestBody<>();
        List<ApiResponse> apis = apiService.getApisByServiceId(serviceId);
        response.setData(apis);
        return response;
    }

    @RequestMapping(value = "/services/apis", method = RequestMethod.GET)
    public RestBody<List<ApiResponse>> getServicesAndApis() throws Exception {
        RestBody<List<ApiResponse>> response = new RestBody<>();
        List<ApiResponse> apis = apiService.getApis();
        response.setData(apis);
        return response;
    }

    @RequestMapping(value = "/apps/apis", method = RequestMethod.GET)
    public RestBody<List<AppApiResponse>> getAppAndApis() throws Exception {
        RestBody<List<AppApiResponse>> response = new RestBody<>();
        List<AppApiResponse> appsAndApis = apiService.getAppsAndApis();
        response.setData(appsAndApis);
        return response;
    }
}
