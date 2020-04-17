package com.nadia.openplatfrom.isv.manage.controller;

import com.nadia.openplatform.common.rest.RestBody;
import com.nadia.openplatfrom.isv.account.exception.AppException;
import com.nadia.openplatfrom.isv.manage.dto.response.MenuResponse;
import com.nadia.openplatfrom.isv.manage.service.MenuInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("/manage")
public class MenuInfoController {
    @Resource
    private MenuInfoService menuInfoService;

    @RequestMapping(value = "/menu/list", method = RequestMethod.GET)
    public RestBody<MenuResponse> getApis(){
        if (SecurityContextHolder.getUserDetail() != null) {
            Long isvId = SecurityContextHolder.getUserDetail().getUid();
            MenuResponse menus = menuInfoService.getMenusByIsvId(isvId);
            RestBody<MenuResponse> response = new RestBody<>();
            response.setData(menus);
            return response;
        } else {
            throw new AppException(1008L);
        }
    }
}
