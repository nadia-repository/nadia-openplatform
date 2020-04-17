package com.nadia.openplatfrom.isv.account.controller;

import com.alibaba.fastjson.JSON;

import com.nadia.openplatform.common.rest.RestBody;
import com.nadia.openplatfrom.isv.account.domain.AppInfo;
import com.nadia.openplatfrom.isv.account.dto.request.AppRequest;
import com.nadia.openplatfrom.isv.account.dto.request.LoginRequest;
import com.nadia.openplatfrom.isv.account.dto.request.RegisterRequest;
import com.nadia.openplatfrom.isv.account.dto.response.ApiResponse;
import com.nadia.openplatfrom.isv.account.dto.response.AppResponse;
import com.nadia.openplatfrom.isv.account.dto.response.LoginResponse;
import com.nadia.openplatfrom.isv.account.dto.response.RegisterResponse;
import com.nadia.openplatfrom.isv.account.exception.AppException;
import com.nadia.openplatfrom.isv.account.service.AccountService;
import com.nadia.openplatfrom.isv.account.service.AppInfoService;
import com.nadia.openplatfrom.isv.manage.service.ApiInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Slf4j
@RestController
@RequestMapping
@Api(tags = "isv center")
public class AccountController {

    @Resource
    private AccountService accountService;
    @Resource
    private AppInfoService appService;
    @Resource
    private ApiInfoService apiInfoService;
    @Resource
    private UidGenerator uidGenerator;

    @ApiOperation("register new ISV")
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public RestBody<RegisterResponse> register(@RequestBody RegisterRequest registerRequest){
        log.info("register registerRequest:{}", JSON.toJSONString(registerRequest));
        RestBody<RegisterResponse> response = new RestBody<>();
        RegisterResponse register = accountService.register(registerRequest);
        response.setData(register);
        return response;
    }

    @ApiOperation("login ISV")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public RestBody<LoginResponse> login(@RequestBody LoginRequest loginRequest){
    log.info("login loginRequest:{}", JSON.toJSONString(loginRequest));
        RestBody<LoginResponse> response = new RestBody<>();
        LoginResponse login = accountService.login(loginRequest);
        response.setData(login);
        return response;
    }

    @ApiOperation("logout ISV")
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public RestBody logout(){
        if (SecurityContextHolder.getUserDetail() != null) {
            Long isvId = SecurityContextHolder.getUserDetail().getUid();
            log.info("logout success,isvId:{}", isvId);

            // logout history
            accountService.saveLoginHistory(isvId, "logout");
        } else {
            log.info("logout getUserDetail Is Null");
        }
        return new RestBody();
    }

    @ApiOperation("apply new APP")
    @RequestMapping(value = "/add/app", method = RequestMethod.POST)
    public RestBody<AppResponse> addApp(@RequestBody AppRequest appRequest){
        log.info("addApp appRequest:{}", JSON.toJSONString(appRequest));
        if (SecurityContextHolder.getUserDetail() != null) {
            Long isvId = SecurityContextHolder.getUserDetail().getUid();
            RestBody<AppResponse> response = new RestBody<>();
            AppResponse app = appService.addApp(isvId,appRequest);
            response.setData(app);
            return response;
        } else {
            throw new AppException(1008L);
        }
    }

    @ApiOperation("modify APP info")
    @RequestMapping(value = "/modify/app", method = RequestMethod.POST)
    public RestBody<AppResponse> modifyApp(@RequestBody AppRequest appRequest){
        log.info("modifyApp appRequest:{}", JSON.toJSONString(appRequest));
        RestBody<AppResponse> response = new RestBody<>();
        AppResponse app = appService.modifyApp(appRequest);
        response.setData(app);
        return response;
    }

    @ApiOperation("get ISV's APP list")
    @RequestMapping(value = "/apps", method = RequestMethod.POST)
    public RestBody<List<AppInfo>> getApps(@RequestBody AppRequest appRequest){
        if (SecurityContextHolder.getUserDetail() != null) {
            Long isvId = SecurityContextHolder.getUserDetail().getUid();
            List<AppInfo> apps = appService.getApps(isvId,appRequest);
            RestBody<List<AppInfo>> response = new RestBody<>();
            response.setData(apps);
            return response;
        } else {
            throw new AppException(1008L);
        }
    }

    @RequestMapping(value = "/allApps", method = RequestMethod.GET)
    public RestBody<List<AppInfo>> getAllApps(){
            List<AppInfo> apps = appService.getAllApps();
            RestBody<List<AppInfo>> response = new RestBody<>();
            response.setData(apps);
            return response;
    }

    @ApiOperation("get ISV's APP by id")
    @RequestMapping(value = "/app/{id}", method = RequestMethod.GET)
    public RestBody<AppResponse> getAppById(@PathVariable("id") Long id){
        if (SecurityContextHolder.getUserDetail() != null) {
            Long isvId = SecurityContextHolder.getUserDetail().getUid();
            AppResponse result = appService.getAppById(isvId, id);
            RestBody<AppResponse> response = new RestBody<>();
            response.setData(result);
            return response;
        } else {
            throw new AppException(1008L);
        }
    }

    @ApiOperation("get ISV's API list")
    @RequestMapping(value = "/apis", method = RequestMethod.POST)
    public RestBody<ApiResponse> getApis(@RequestBody AppRequest appRequest){
        if (SecurityContextHolder.getUserDetail() != null) {
            Long isvId = SecurityContextHolder.getUserDetail().getUid();
            ApiResponse apiInfoByApp = apiInfoService.getApiInfoByApp(isvId,appRequest);
            RestBody<ApiResponse> response = new RestBody<>();
            response.setData(apiInfoByApp);
            return response;
        } else {
            throw new AppException(1008L);
        }
    }

    @ApiOperation("get generate appkey")
    @RequestMapping(value = "/genAppKey", method = RequestMethod.GET)
    public RestBody<String> genAppKey(){
        String appKey = new SimpleDateFormat("yyyyMMdd").format(new Date()) + uidGenerator.getUID();
        RestBody<String> response = new RestBody<>();
        response.setData(appKey);
        return response;
    }
}
