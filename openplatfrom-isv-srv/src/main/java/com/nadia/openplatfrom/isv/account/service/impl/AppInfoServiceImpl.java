package com.nadia.openplatfrom.isv.account.service.impl;

import com.alibaba.fastjson.JSON;
import com.nadia.openplatfrom.isv.account.dao.AppInfoMapper;
import com.nadia.openplatfrom.isv.account.domain.AppBusinessPackage;
import com.nadia.openplatfrom.isv.account.domain.AppInfo;
import com.nadia.openplatfrom.isv.account.domain.criteria.AppInfoCriteria;
import com.nadia.openplatfrom.isv.account.dto.request.AppRequest;
import com.nadia.openplatfrom.isv.account.dto.response.AppResponse;
import com.nadia.openplatfrom.isv.account.enums.AppEnum;
import com.nadia.openplatfrom.isv.account.exception.AppException;
import com.nadia.openplatfrom.isv.account.service.AppBusinessPackageService;
import com.nadia.openplatfrom.isv.account.service.AppInfoService;
import com.nadia.openplatfrom.isv.bean.ChannelMsg;
import com.nadia.openplatfrom.isv.bean.ZookeeperContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AppInfoServiceImpl implements AppInfoService {
    @Resource
    private AppInfoMapper appInfoMapper;
    @ResourceZookeeperTool
    private AppBusinessPackageService appBusinessPackageService;
    @Resource
    private ZookeeperTool zookeeperTool;

    @Override
    public AppResponse addApp(Long isvId, AppRequest appRequest) {
        AppInfoCriteria appExample = new AppInfoCriteria();
        appExample.createCriteria().andNameEqualTo(appRequest.getName());
        long cnt = appInfoMapper.countByExample(appExample);
        if(cnt > 0){
            throw new AppException(1012L);
        }

        AppInfo record = new AppInfo();
        BeanUtils.copyProperties(appRequest,record);
        record.setIsvId(isvId);
        record.setStatus(appRequest.getStatus().getCode());
        appInfoMapper.insertSelective(record);
        this.sendChannelMsg(record);
        if(CollectionUtils.isNotEmpty(appRequest.getBusinessPackageId())){
            for(Long id : appRequest.getBusinessPackageId()){
                AppBusinessPackage appBusinessPackage = new AppBusinessPackage();
                appBusinessPackage.setAppId(record.getId());
                appBusinessPackage.setBusinessPackageId(id);
                appBusinessPackage.setStatus(AppEnum.ENABLE.getCode());
                appBusinessPackageService.save(appBusinessPackage);
            }
        }

        AppResponse response = new AppResponse();
        BeanUtils.copyProperties(response,record);
        return response;
    }

    @Override
    public AppResponse modifyApp(AppRequest appRequest) {
        AppInfo record = new AppInfo();
        BeanUtils.copyProperties(appRequest,record);
        record.setStatus(appRequest.getStatus().getCode());
        int cnt = appInfoMapper.updateByPrimaryKeySelective(record);
        if(cnt == 0){
            throw new AppException(1007L);
        }

        AppInfo appInfo = appInfoMapper.selectByPrimaryKey(appRequest.getId());
        this.sendChannelMsg(appInfo);

        appBusinessPackageService.deleteByAppId(appRequest.getId());
        if(CollectionUtils.isNotEmpty(appRequest.getBusinessPackageId())){
            for(Long id : appRequest.getBusinessPackageId()){
                AppBusinessPackage appBusinessPackage = new AppBusinessPackage();
                appBusinessPackage.setAppId(appRequest.getId());
                appBusinessPackage.setBusinessPackageId(id);
                appBusinessPackage.setStatus(AppEnum.ENABLE.getCode());
                appBusinessPackageService.save(appBusinessPackage);
            }
        }

        AppResponse response = new AppResponse();
        BeanUtils.copyProperties(response,record);
        return response;
    }

    @Override
    public List<AppInfo> getApps(Long isvId,AppRequest appRequest) {
        AppInfoCriteria example = new AppInfoCriteria();
        AppInfoCriteria.Criteria criteria = example.createCriteria();
        criteria.andIsvIdEqualTo(isvId);
        if(StringUtils.isNotBlank(appRequest.getAppKey())){
            criteria.andAppKeyLike(appRequest.getAppKey());
        }else if(StringUtils.isNotBlank(appRequest.getName())){
            criteria.andNameLike(appRequest.getName());
        }
        List<AppInfo> apps = appInfoMapper.selectByExampleWithBLOBs(example);
        return apps;
    }

    @Override
    public List<AppInfo> getAllApps() {
        AppInfoCriteria example = new AppInfoCriteria();
        List<AppInfo> apps = appInfoMapper.selectByExampleWithBLOBs(example);
        return apps;
    }

    @Override
    public AppResponse getAppById(Long isvId, Long id) {
        AppInfo appInfo = appInfoMapper.selectByPrimaryKey(id);
        AppResponse response = new AppResponse();
        BeanUtils.copyProperties(appInfo,response);
        List<AppBusinessPackage> appBusinessPackageByAppId = appBusinessPackageService.getAppBusinessPackageByAppId(appInfo.getId());
        List<Long> businessPackageIds = appBusinessPackageByAppId.stream().map(item -> item.getBusinessPackageId()).collect(Collectors.toList());
        response.setBusinessPackageId(businessPackageIds);
        return response;
    }

    private void sendChannelMsg(AppInfo rec) {
        ChannelMsg channelMsg = new ChannelMsg("update", rec);
        String path = ZookeeperContext.getAppInfoChannelPath();
        String data = JSON.toJSONString(channelMsg);
        try {
            log.info("消息推送--ISV信息(update), path:{}, data:{}", path, data);
            zookeeperTool.createOrUpdateData(path, data);
        } catch (Exception e) {
            log.error("发送isvChannelMsg失败, path:{}, msg:{}", path, data, e);
        }
    }
}
