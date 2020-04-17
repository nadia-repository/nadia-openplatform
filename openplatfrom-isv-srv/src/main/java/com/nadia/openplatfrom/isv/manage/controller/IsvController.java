package com.nadia.openplatfrom.isv.manage.controller;

import com.nadia.openplatform.common.rest.RestBody;
import com.nadia.openplatform.common.utils.BeanMapUtils;
import com.nadia.openplatfrom.isv.account.domain.AppInfo;
import com.nadia.openplatfrom.isv.account.domain.IsvInfo;
import com.nadia.openplatfrom.isv.account.dto.request.AppRequest;
import com.nadia.openplatfrom.isv.account.service.AccountService;
import com.nadia.openplatfrom.isv.account.service.AppInfoService;
import com.nadia.openplatfrom.isv.manage.dto.request.IsvRequest;
import com.nadia.openplatfrom.isv.manage.dto.response.IsvsResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/manage")
public class IsvController {

    @Resource
    private AccountService accountService;
    @Resource
    private AppInfoService appInfoService;

    @RequestMapping(value = "/isvs", method = RequestMethod.POST)
    public RestBody<List<IsvsResponse>> getIsvs(@RequestBody IsvRequest isvRequest){
        List<IsvInfo> isvs = accountService.getIsvs(isvRequest);
        List<IsvsResponse> info = BeanMapUtils.mapList(isvs, IsvsResponse.class);
        RestBody<List<IsvsResponse>> response = new RestBody<>();
        response.setData(info);
        return response;
    }

    @RequestMapping(value = "/isv/apps/{isvId}", method = RequestMethod.GET)
    public RestBody<List<AppInfo>> getAppsOfIsv(@PathVariable("isvId") Long isvId){
        log.info("getAppsOfIsv isvId:{}",isvId);
        List<AppInfo> apps = appInfoService.getApps(isvId,new AppRequest());
        RestBody<List<AppInfo>> response = new RestBody<>();
        response.setData(apps);
        return response;
    }

    @RequestMapping(value = "/confirm/isv/{isvId}", method = RequestMethod.GET)
    public void confirmIsv(@PathVariable("isvId") Long isvId){
        log.info("confirmIsv isvId:{}",isvId);
        accountService.activateIsv(isvId);
    }
}
