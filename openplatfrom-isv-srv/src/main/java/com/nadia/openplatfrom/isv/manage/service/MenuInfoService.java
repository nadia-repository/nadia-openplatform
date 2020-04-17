package com.nadia.openplatfrom.isv.manage.service;


import com.nadia.openplatfrom.isv.manage.dto.response.MenuResponse;

public interface MenuInfoService {
    MenuResponse getMenusByIsvId(Long isvId);
}
