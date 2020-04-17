package com.nadia.openplatfrom.isv.manage.service.impl;

import com.nadia.openplatform.common.utils.BeanMapUtils;
import com.nadia.openplatfrom.isv.account.domain.AppBusinessPackage;
import com.nadia.openplatfrom.isv.account.domain.AppInfo;
import com.nadia.openplatfrom.isv.account.dto.request.AppRequest;
import com.nadia.openplatfrom.isv.account.dto.response.ApiResponse;
import com.nadia.openplatfrom.isv.account.dto.response.BusinessBackageApiResponse;
import com.nadia.openplatfrom.isv.account.service.AppBusinessPackageService;
import com.nadia.openplatfrom.isv.account.service.AppInfoService;
import com.nadia.openplatfrom.isv.manage.dao.ApiInfoMapper;
import com.nadia.openplatfrom.isv.manage.domain.ApiInfo;
import com.nadia.openplatfrom.isv.manage.domain.BusinessPackage;
import com.nadia.openplatfrom.isv.manage.domain.criteria.ApiInfoCriteria;
import com.nadia.openplatfrom.isv.manage.dto.request.ApiInfoRequest;
import com.nadia.openplatfrom.isv.manage.enums.ApiEnum;
import com.nadia.openplatfrom.isv.manage.service.ApiInfoService;
import com.nadia.openplatfrom.isv.manage.service.BusinessPackageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service
@Slf4j
public class ApiInfoServiceImpl implements ApiInfoService {
    @Resource
    private ApiInfoMapper apiInfoMapper;
    @Resource
    private BusinessPackageService businessPackageService;
    @Resource
    private AppInfoService appInfoService;
    @Resource
    private AppBusinessPackageService appBusinessPackageService;

    @Override
    public List<ApiInfo> getApiInfosByBusinessPackageIds(List<Long> ids) {
        ApiInfoCriteria example = new ApiInfoCriteria();
        example.createCriteria().andBusinessPackageIdIn(ids);
        return apiInfoMapper.selectByExample(example);
    }

    @Override
    public List<ApiInfo> getApiInfosByBusinessPackageId(Long id) {
        ApiInfoCriteria example = new ApiInfoCriteria();
        example.createCriteria().andBusinessPackageIdEqualTo(id);
        return apiInfoMapper.selectByExample(example);
    }

    @Override
    public ApiResponse getApiInfoByApp(Long isvId, AppRequest appRequest) {
        List<AppInfo> apps = appInfoService.getApps(isvId,appRequest);
        ApiResponse response = new ApiResponse();

        if(!CollectionUtils.isEmpty(apps)){
            List<ApiResponse.AppDto> appDtos = new LinkedList<>();
            for(AppInfo app : apps){
                //app
                ApiResponse.AppDto dto = new ApiResponse.AppDto();
                BeanUtils.copyProperties(app,dto);
                //BusinessPackages
                this.packageBusinessBackage(dto);
                appDtos.add(dto);
            }
            response.setApps(appDtos);
        }
        return response;
    }

    private void packageBusinessBackage(ApiResponse.AppDto dto){
        List<AppBusinessPackage> appBusinessPackages = appBusinessPackageService.getAppBusinessPackageByAppId(dto.getId());
        if(!CollectionUtils.isEmpty(appBusinessPackages)){
            List<ApiResponse.BusinessPackageDto> businessBackageInfos = new LinkedList<>();
            for(AppBusinessPackage appBusinessPackage :appBusinessPackages){
                BusinessPackage businessPackage = businessPackageService.getBusinessPackageById(appBusinessPackage.getBusinessPackageId());
                ApiResponse.BusinessPackageDto businessPackageDto = new ApiResponse.BusinessPackageDto();
                BeanUtils.copyProperties(businessPackage, businessPackageDto);
                //api
                this.packageApis(businessPackageDto);

                businessBackageInfos.add(businessPackageDto);
            }
            dto.setBusinessPackageDtos(businessBackageInfos);
        }
    }

    private void packageApis(ApiResponse.BusinessPackageDto businessPackageDto){
        ApiInfoCriteria example = new ApiInfoCriteria();
        example.createCriteria().andBusinessPackageIdEqualTo(businessPackageDto.getId());
        List<ApiInfo> apiInfos = apiInfoMapper.selectByExample(example);
        if(!CollectionUtils.isEmpty(apiInfos)){
            List<ApiResponse.ApiInfoDto> apis = new LinkedList<>();
            for(ApiInfo api : apiInfos){
                ApiResponse.ApiInfoDto apiDto = new ApiResponse.ApiInfoDto();
                BeanUtils.copyProperties(api,apiDto);
                apis.add(apiDto);
            }
            businessPackageDto.setApiInfos(apis);
        }
    }

    @Override
    public BusinessBackageApiResponse getApiInfos() {
        List<BusinessPackage> serviceInfoByIsv = businessPackageService.getBusinessPackages();
        BusinessBackageApiResponse response = new BusinessBackageApiResponse();

        List<BusinessBackageApiResponse.BusinessBackageDto> bbDto = new ArrayList<>();
        for(BusinessPackage businessPackage : serviceInfoByIsv){
            BusinessBackageApiResponse.BusinessBackageDto bb = new BusinessBackageApiResponse.BusinessBackageDto();
            BeanUtils.copyProperties(businessPackage,bb);

            List<ApiInfo> apiInfos = this.getApiInfosByBusinessPackageId(businessPackage.getId());
            List<BusinessBackageApiResponse.ApiInfoDto> apiInfoDtos = BeanMapUtils.mapList(apiInfos, BusinessBackageApiResponse.ApiInfoDto.class);
            bb.setApiInfos(apiInfoDtos);
            bbDto.add(bb);
        }
        response.setBusinessBackageInfos(bbDto);
        return response;
    }

    @Override
    public void addApiInfos(Long isvId,List<ApiInfoRequest> apiInfos) {
        for(ApiInfoRequest apiInfo : apiInfos){
            ApiInfo record = new ApiInfo();
            record.setBusinessPackageId(isvId);
            record.setName(apiInfo.getName());
            record.setSrvName(apiInfo.getSrvName());
            record.setVersion(apiInfo.getVersion());
            record.setStatus(ApiEnum.ENABLE.getCode());
            apiInfoMapper.insertSelective(record);
        }
    }

    @Override
    public int deleteApisByBusinessPackageId(Long businessPackageId) {
        ApiInfoCriteria example = new ApiInfoCriteria();
        example.createCriteria().andBusinessPackageIdEqualTo(businessPackageId);
        int cnt = apiInfoMapper.deleteByExample(example);
        return cnt;
    }

    @Override
    public void apiInfo(String name) {

    }
}
