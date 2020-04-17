package com.nadia.openplatfrom.isv.manage.controller;

import com.alibaba.fastjson.JSON;
import com.nadia.openplatform.common.rest.RestBody;
import com.nadia.openplatform.common.utils.BeanMapUtils;
import com.nadia.openplatfrom.isv.account.dto.response.ApiResponse;
import com.nadia.openplatfrom.isv.account.dto.response.BusinessBackageApiResponse;
import com.nadia.openplatfrom.isv.account.dto.response.BusinessBackageResponse;
import com.nadia.openplatfrom.isv.manage.domain.BusinessPackage;
import com.nadia.openplatfrom.isv.manage.dto.request.BusinessPackageRequest;
import com.nadia.openplatfrom.isv.manage.dto.response.BusinessPackagesAndApisResponse;
import com.nadia.openplatfrom.isv.manage.service.ApiInfoService;
import com.nadia.openplatfrom.isv.manage.service.BusinessPackageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/manage")
public class BusinessPackageController {

    @Resource
    private BusinessPackageService businessPackageService;
    @Resource
    private ApiInfoService apiInfoService;
    @Resource
    private ZookeeperTool zookeeperTool;

    @RequestMapping(value = "/businessPackage/apis", method = RequestMethod.GET)
    public RestBody<BusinessBackageApiResponse> getApis(){
        RestBody<BusinessBackageApiResponse> response = new RestBody<>();
        BusinessBackageApiResponse apiInfos = apiInfoService.getApiInfos();
        response.setData(apiInfos);
        return response;
    }

    @RequestMapping(value = "/businessPackage/add", method = RequestMethod.POST)
    public RestBody<String> addBusinessPackage(@RequestBody BusinessPackageRequest businessPackageRequest){
        log.info("addBusinessPackage request:{}", JSON.toJSONString(businessPackageRequest));
        RestBody<String> response = new RestBody();
        businessPackageService.addBusinessPackage(businessPackageRequest);
        response.setData("success");
        return response;
    }

    @RequestMapping(value = "/businessPackage/modify", method = RequestMethod.POST)
    public RestBody<String> modifyBusinessPackage(@RequestBody BusinessPackageRequest businessPackageRequest){
        log.info("modifyBusinessPackage request:{}", JSON.toJSONString(businessPackageRequest));
        RestBody<String> response = new RestBody();
        businessPackageService.modifyBusinessPackage(businessPackageRequest);
        response.setData("success");
        return response;
    }

    @RequestMapping(value = "/businessPackages", method = RequestMethod.GET)
    public RestBody<List<BusinessBackageResponse>> getBusinessPackages(){
        RestBody<List<BusinessBackageResponse>> response = new RestBody<>();
        List<BusinessPackage> businessPackages = businessPackageService.getBusinessPackages();
        List<BusinessBackageResponse> businessBackageResponses = BeanMapUtils.mapList(businessPackages, BusinessBackageResponse.class);
        response.setData(businessBackageResponses);
        return response;
    }

    @RequestMapping(value = "/businessPackages/apis", method = RequestMethod.POST)
    public RestBody<List<ApiResponse.BusinessPackageDto>> getBusinessPackagesAndApis(@RequestBody BusinessPackageRequest businessPackageRequest){
        RestBody<List<ApiResponse.BusinessPackageDto>> response = new RestBody<List<ApiResponse.BusinessPackageDto>>();
        List<ApiResponse.BusinessPackageDto> businessPackagesAndApis = businessPackageService.getBusinessPackagesAndApis();
        response.setData(businessPackagesAndApis);
        return response;
    }

    @RequestMapping(value = "/businessPackage/apis/{id}", method = RequestMethod.GET)
    public RestBody<List<BusinessPackagesAndApisResponse>> getApis(@PathVariable("id") Long id) throws Exception {
        RestBody<List<BusinessPackagesAndApisResponse>> response = new RestBody<>();
        List<BusinessPackagesAndApisResponse> businessPackagesAndApisById = businessPackageService.getBusinessPackagesAndApisById(id);
        response.setData(businessPackagesAndApisById);
        return response;
    }
}
