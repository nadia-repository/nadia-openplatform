package com.nadia.openplatfrom.isv.account.service.impl;

import com.nadia.openplatform.common.utils.StringUtils;
import com.nadia.openplatfrom.isv.account.dao.IsvInfoMapper;
import com.nadia.openplatfrom.isv.account.dao.LoginHistoriesMapper;
import com.nadia.openplatfrom.isv.account.domain.IsvInfo;
import com.nadia.openplatfrom.isv.account.domain.LoginHistories;
import com.nadia.openplatfrom.isv.account.domain.criteria.IsvInfoCriteria;
import com.nadia.openplatfrom.isv.account.dto.request.LoginRequest;
import com.nadia.openplatfrom.isv.account.dto.request.RegisterRequest;
import com.nadia.openplatfrom.isv.account.dto.response.LoginResponse;
import com.nadia.openplatfrom.isv.account.dto.response.RegisterResponse;
import com.nadia.openplatfrom.isv.account.enums.IsvEnum;
import com.nadia.openplatfrom.isv.account.exception.AccountException;
import com.nadia.openplatfrom.isv.account.properties.AccountProperties;
import com.nadia.openplatfrom.isv.account.service.AccountService;
import com.nadia.openplatfrom.isv.manage.dto.request.IsvRequest;
import com.nadia.openplatfrom.isv.manage.enums.RoleEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class AccountServiceImpl implements AccountService {

    @Resource
    private IsvInfoMapper isvInfoMapper;
    @Resource
    private CacheService cacheService;
    @Resource
    private AccountProperties accountProperties;
    @Resource
    private LoginHistoriesMapper loginHistoriesMapper;
    @Resource
    private PermissionTokenService permissionTokenService;

    @Override
    public RegisterResponse register(RegisterRequest registerRequest) {
        //check name
        IsvInfoCriteria example = new IsvInfoCriteria();
        example.createCriteria().andNameEqualTo(registerRequest.getName());
        long nameCnt = isvInfoMapper.countByExample(example);
        if(nameCnt > 0){
            throw new AccountException(1001L);
        }
        //check mobile
        example.clear();
        example.createCriteria().andMobileEqualTo(registerRequest.getMobile());
        long mobileCnt = isvInfoMapper.countByExample(example);
        if(mobileCnt > 0){
            throw new AccountException(1002L);
        }
        //check mail
        example.clear();
        example.createCriteria().andEmailEqualTo(registerRequest.getMail());
        long mailCnt = isvInfoMapper.countByExample(example);
        if(mailCnt > 0){
            throw new AccountException(1003L);
        }

        IsvInfo record = new IsvInfo();
        BeanUtils.copyProperties(registerRequest,record);
        record.setStatus(IsvEnum.DISABLE.getCode());
        record.setCreatedBy("admin");
        record.setUpdatedBy("admin");
        record.setEmail(registerRequest.getMail());
        record.setRoleId(Long.valueOf(RoleEnum.ISV.getCode()));
        int id = insertIsv(record);
        if(id == 0){
            throw new AccountException(1004L);
        }

        RegisterResponse response = new RegisterResponse();
        BeanUtils.copyProperties(record,response);
        response.setId(Long.valueOf(id));
        return response;
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        if(getLeftFailTimes(loginRequest.getName()) == 0){
            throw new AccountException(1005L);
        }

        IsvInfo isvInfo = getIsvByName(loginRequest.getName());
        if (isvInfo == null || !BCrypt.checkpw(loginRequest.getPassword(), isvInfo.getPassword())) {
            processLoginFailTimes(loginRequest.getName());
        }

        saveLoginHistory(isvInfo.getId(),"login");
        LoginResponse response = new LoginResponse();
        BeanUtils.copyProperties(isvInfo,response);

        Map<String, String> map = permissionTokenService.afterLogin(isvInfo.getId());
        response.setAccessToken(map.get("access_token"));
        response.setRefreshToken(map.get("refresh_token"));
        return response;
    }

    public static void main(String[] args){
        String p1 = "1234";
        String p2 = "$2a$10$ZAq/NNHrXxIbZUCEZxyfEOetyGMh.sNsyXkx3Bnm6dziYh4qVf9Bi";
        String hashpw = BCrypt.hashpw(p1, BCrypt.gensalt(10));
        boolean checkpw = BCrypt.checkpw(p1, p2);
        boolean checkpw1 = BCrypt.checkpw(p1, hashpw);
        System.out.println(checkpw);
        System.out.println(checkpw1);
    }

    @Override
    public int insertIsv(IsvInfo record){
        String hashpw = BCrypt.hashpw(record.getPassword(), BCrypt.gensalt(10));
        log.info("hashpw:{}",hashpw);
        record.setPassword(hashpw);
        int id = isvInfoMapper.insertSelective(record);
        return id;
    }

    @Override
    public int updateIsv(IsvInfo record){
        record.setPassword(BCrypt.hashpw(record.getPassword(), BCrypt.gensalt(10)));
        int cnt = isvInfoMapper.updateByPrimaryKeySelective(record);
        return cnt;
    }



    private int getLeftFailTimes(String name){
        String key = name + "_login_failed";
        if (cacheService.exists(key)) {
            int failTimes = Integer.parseInt(cacheService.get(key));
            if (failTimes <= accountProperties.getMaxLoginFailTimes()) {
                return accountProperties.getMaxLoginFailTimes() - failTimes;
            }
            return 0;
        }
        return accountProperties.getMaxLoginFailTimes();
    }

    @Override
    public IsvInfo getIsvByName(String name) {
        IsvInfoCriteria example = new IsvInfoCriteria();
        example.createCriteria().andNameEqualTo(name);
        List<IsvInfo> isvs = isvInfoMapper.selectByExample(example);
        if(CollectionUtils.isEmpty(isvs)){
            return null;
        }else {
            return isvs.get(0);
        }
    }

    private IsvInfo processLoginFailTimes(String name) {
        processFailTimes(name);
        int leftFailTimes = getLeftFailTimes(name);
        Object[] args = new Object[1];
        args[0] = leftFailTimes;
        throw new AccountException(1006L, args);
    }

    private void processFailTimes(String name) {
        String key = name + "_login_failed";
        if (cacheService.exists(key)) {
            cacheService.incr(key);
            int failTimes = Integer.parseInt(cacheService.get(key));
            if (failTimes >= accountProperties.getMaxLoginFailTimes()) {
                throw new AccountException(1005L);
            }
        } else {
            cacheService.set(key, "1", 3 * 60 * 60);
        }
    }

    @Override
    public void saveLoginHistory(Long isvId, String action) {
        LoginHistories record = new LoginHistories();
        record.setAction(action);
        record.setIsvId(isvId);
        loginHistoriesMapper.insertSelective(record);
    }

    @Override
    public List<IsvInfo> getIsvs(IsvRequest isvRequest) {
        IsvInfoCriteria isvInfoCriteria = new IsvInfoCriteria();
        IsvInfoCriteria.Criteria criteria = isvInfoCriteria.createCriteria();
        criteria.andRoleIdEqualTo(Long.valueOf(RoleEnum.ISV.getCode()));
        if(StringUtils.isNotBlank(isvRequest.getMail())){
            criteria.andEmailLike(isvRequest.getMail());
        }else if(StringUtils.isNotBlank(isvRequest.getName())){
            criteria.andNameLike(isvRequest.getName());
        }else if(StringUtils.isNotBlank(isvRequest.getMobile())){
            criteria.andMobileLike(isvRequest.getMobile());
        }
        return isvInfoMapper.selectByExample(isvInfoCriteria);
    }

    @Override
    public void activateIsv(Long isvId) {
        IsvInfo info = new IsvInfo();
        info.setId(isvId);
        info.setStatus(IsvEnum.ENABLE.getCode());
        isvInfoMapper.updateByPrimaryKeySelective(info);

        //todo 发邮件
    }

    @Override
    public IsvInfo getIsvById(Long IsvId) {
        return isvInfoMapper.selectByPrimaryKey(IsvId);
    }
}
