package com.nadia.openplatfrom.isv.manage.service;


import com.nadia.openplatfrom.isv.manage.dto.response.ApiResponse;
import com.nadia.openplatfrom.isv.manage.dto.response.AppApiResponse;
import com.nadia.openplatfrom.isv.manage.dto.response.ServiceResponse;

import java.util.List;

public interface ApiService {

    List<ServiceResponse> getServices() throws Exception;

    List<ApiResponse> getApisByServiceId(String serviceId) throws Exception;

    List<ApiResponse> getApis() throws Exception;

    List<ServiceResponse> getServicesAndApis() throws Exception;

    List<AppApiResponse> getAppsAndApis() throws Exception;
}
