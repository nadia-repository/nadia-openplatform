package com.nadia.openplatfrom.isv.manage.service.impl;

import com.alibaba.fastjson.JSON;
import com.nadia.openplatform.common.utils.BeanMapUtils;
import com.nadia.openplatfrom.isv.account.domain.AppInfo;
import com.nadia.openplatfrom.isv.account.domain.AppRoutePermission;
import com.nadia.openplatfrom.isv.account.dto.request.AppRequest;
import com.nadia.openplatfrom.isv.account.dto.response.ApiResponse;
import com.nadia.openplatfrom.isv.account.service.AppInfoService;
import com.nadia.openplatfrom.isv.bean.ChannelMsg;
import com.nadia.openplatfrom.isv.bean.ZookeeperContext;
import com.nadia.openplatfrom.isv.manage.dao.BusinessPackageMapper;
import com.nadia.openplatfrom.isv.manage.domain.ApiInfo;
import com.nadia.openplatfrom.isv.manage.domain.BusinessPackage;
import com.nadia.openplatfrom.isv.manage.domain.criteria.BusinessPackageCriteria;
import com.nadia.openplatfrom.isv.manage.dto.request.BusinessPackageRequest;
import com.nadia.openplatfrom.isv.manage.dto.response.BusinessPackagesAndApisResponse;
import com.nadia.openplatfrom.isv.manage.enums.BusinessPackageEnum;
import com.nadia.openplatfrom.isv.manage.exception.BusinessPackageException;
import com.nadia.openplatfrom.isv.manage.service.ApiInfoService;
import com.nadia.openplatfrom.isv.manage.service.ApiService;
import com.nadia.openplatfrom.isv.manage.service.BusinessPackageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BusinessPackageServiceImpl implements BusinessPackageService {
    @Resource
    private BusinessPackageMapper businessPackageMapper;
    @Resource
    private AppInfoService appInfoService;
    @Resource
    private ApiInfoService apiInfoService;
    @Resource
    private ApiService apiService;
    @Resource
    private ZookeeperTool zookeeperTool;

    @Override
    public List<BusinessPackage> getBusinessPackageById(List<Long> ids) {
        BusinessPackageCriteria example = new BusinessPackageCriteria();
        example.createCriteria().andIdIn(ids);
        return businessPackageMapper.selectByExample(example);
    }

    @Override
    public List<BusinessPackage> getBusinessPackageByIsv(Long isvId) {

        List<AppInfo> apps = appInfoService.getApps(isvId,new AppRequest());
        List<Long> ids = apps.stream().map(c -> c.getBusinessPackageId()).collect(Collectors.toList());

        BusinessPackageCriteria example = new BusinessPackageCriteria();
        example.createCriteria().andIdIn(ids);
        return businessPackageMapper.selectByExample(example);
    }

    @Override
    public BusinessPackage getBusinessPackageById(Long id) {
        return businessPackageMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<BusinessPackage> getBusinessPackageByName(String name) {
        BusinessPackageCriteria example = new BusinessPackageCriteria();
        example.createCriteria().andNameEqualTo(name);
        List<BusinessPackage> businessPackages = businessPackageMapper.selectByExample(example);
        return businessPackages;
    }

    @Override
    public List<BusinessPackage> getBusinessPackages() {
        return businessPackageMapper.selectByExample(new BusinessPackageCriteria());
    }

    @Override
    public void addBusinessPackage(BusinessPackageRequest businessPackageRequest) {
        String name = businessPackageRequest.getName();
        List<BusinessPackage> businessPackageByName = this.getBusinessPackageByName(name);
        if(!CollectionUtils.isEmpty(businessPackageByName) && businessPackageByName.size() > 0){
            throw new BusinessPackageException(1011L);
        }
        BusinessPackage record = new BusinessPackage();
        record.setName(businessPackageRequest.getName());
        record.setStatus(BusinessPackageEnum.ENABLE.getCode());
        int id = businessPackageMapper.insertSelective(record);
        if(id > 0){
            apiInfoService.addApiInfos(record.getId(),businessPackageRequest.getApiInfos());
        }
        sendChannelMsg(new AppRoutePermission());
    }

    @Override
    public void modifyBusinessPackage(BusinessPackageRequest businessPackageRequest) {
        apiInfoService.deleteApisByBusinessPackageId(businessPackageRequest.getId());
        apiInfoService.addApiInfos(businessPackageRequest.getId(),businessPackageRequest.getApiInfos());
        sendChannelMsg(new AppRoutePermission());
    }

    @Override
    public List<ApiResponse.BusinessPackageDto> getBusinessPackagesAndApis() {
        BusinessPackageCriteria businessPackageCriteria = new BusinessPackageCriteria();
        List<BusinessPackage> businessPackages = businessPackageMapper.selectByExample(businessPackageCriteria);
        List<ApiResponse.BusinessPackageDto> response =new LinkedList<>();
        if(!CollectionUtils.isEmpty(businessPackages)){
            businessPackages.forEach(businessPackage -> {
                ApiResponse.BusinessPackageDto businessPackageDto = new ApiResponse.BusinessPackageDto();
                BeanUtils.copyProperties(businessPackage,businessPackageDto);
                List<ApiInfo> apiInfosByBusinessPackageId = apiInfoService.getApiInfosByBusinessPackageId(businessPackage.getId());
                List<ApiResponse.ApiInfoDto> apiInfoDtos = BeanMapUtils.mapList(apiInfosByBusinessPackageId, ApiResponse.ApiInfoDto.class);
                businessPackageDto.setApiInfos(apiInfoDtos);
                response.add(businessPackageDto);
            });
        }
        return response;
    }

    @Override
    public List<BusinessPackagesAndApisResponse> getBusinessPackagesAndApisById(Long id) throws Exception {
        List<ApiInfo> ApiInfos = apiInfoService.getApiInfosByBusinessPackageId(id);
        List<String> apiNames = ApiInfos.stream().map(c -> c.getName()).collect(Collectors.toList());
        List<BusinessPackagesAndApisResponse> responses = new LinkedList<>();
        List<com.nadia.openplatfrom.isv.manage.dto.response.ApiResponse> apis = apiService.getApis();
        apis.forEach(api -> {
            BusinessPackagesAndApisResponse data = new BusinessPackagesAndApisResponse();
            BeanUtils.copyProperties(api,data);
            data.setChecked(apiNames.contains(api.getName()));
            responses.add(data);
        });
        return responses;
    }

    private void sendChannelMsg(AppRoutePermission rec) {
        ChannelMsg channelMsg = new ChannelMsg("reload", rec);
        String path = ZookeeperContext.getIsvRoutePermissionChannelPath();
        String data = JSON.toJSONString(channelMsg);
        try {
            log.info("消息推送--ISV信息(update), path:{}, data:{}", path, data);
            zookeeperTool.createOrUpdateData(path, data);
        } catch (Exception e) {
            log.error("发送isvChannelMsg失败, path:{}, msg:{}", path, data, e);
        }
    }
}
