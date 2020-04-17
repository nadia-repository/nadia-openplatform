package com.nadia.openplatfrom.isv.account.service;



import com.nadia.openplatfrom.isv.account.domain.IsvInfo;
import com.nadia.openplatfrom.isv.account.dto.request.LoginRequest;
import com.nadia.openplatfrom.isv.account.dto.request.RegisterRequest;
import com.nadia.openplatfrom.isv.account.dto.response.LoginResponse;
import com.nadia.openplatfrom.isv.account.dto.response.RegisterResponse;
import com.nadia.openplatfrom.isv.manage.dto.request.IsvRequest;

import java.util.List;

public interface AccountService {

    RegisterResponse register(RegisterRequest registerRequest);

    LoginResponse login(LoginRequest loginRequest);

    int insertIsv(IsvInfo record);

    int updateIsv(IsvInfo record);

    IsvInfo getIsvByName(String name);

    void saveLoginHistory(Long isvId, String action);

    List<IsvInfo> getIsvs(IsvRequest isvRequest);

    void activateIsv(Long isvId);

    IsvInfo getIsvById(Long IsvId);
}
