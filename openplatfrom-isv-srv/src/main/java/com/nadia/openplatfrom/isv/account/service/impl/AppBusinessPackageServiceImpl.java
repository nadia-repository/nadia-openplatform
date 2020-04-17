package com.nadia.openplatfrom.isv.account.service.impl;

import com.nadia.openplatfrom.isv.account.dao.AppBusinessPackageMapper;
import com.nadia.openplatfrom.isv.account.domain.AppBusinessPackage;
import com.nadia.openplatfrom.isv.account.domain.criteria.AppBusinessPackageCriteria;
import com.nadia.openplatfrom.isv.account.service.AppBusinessPackageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
@Slf4j
public class AppBusinessPackageServiceImpl implements AppBusinessPackageService {
    @Resource
    private AppBusinessPackageMapper appBusinessPackageMapper;

    @Override
    public List<AppBusinessPackage> getAppBusinessPackageByAppId(Long appId) {
        AppBusinessPackageCriteria example = new AppBusinessPackageCriteria();
        example.createCriteria().andAppIdEqualTo(appId);
        return appBusinessPackageMapper.selectByExample(example);
    }

    @Override
    public int save(AppBusinessPackage record) {
        return appBusinessPackageMapper.insertSelective(record);
    }

    @Override
    public void deleteByAppId(Long appId) {
        AppBusinessPackageCriteria example = new AppBusinessPackageCriteria();
        example.createCriteria().andAppIdEqualTo(appId);
        appBusinessPackageMapper.deleteByExample(example);
    }
}
