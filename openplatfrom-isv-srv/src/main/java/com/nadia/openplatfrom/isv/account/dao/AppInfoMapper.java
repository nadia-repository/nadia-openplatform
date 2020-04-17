package com.nadia.openplatfrom.isv.account.dao;

import com.nadia.openplatfrom.isv.account.domain.AppInfo;
import com.nadia.openplatfrom.isv.account.domain.criteria.AppInfoCriteria;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AppInfoMapper {
    long countByExample(AppInfoCriteria example);

    int deleteByExample(AppInfoCriteria example);

    int deleteByPrimaryKey(Long id);

    int insert(AppInfo record);

    int insertSelective(AppInfo record);

    List<AppInfo> selectByExampleWithBLOBs(AppInfoCriteria example);

    List<AppInfo> selectByExample(AppInfoCriteria example);

    AppInfo selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") AppInfo record, @Param("example") AppInfoCriteria example);

    int updateByExampleWithBLOBs(@Param("record") AppInfo record, @Param("example") AppInfoCriteria example);

    int updateByExample(@Param("record") AppInfo record, @Param("example") AppInfoCriteria example);

    int updateByPrimaryKeySelective(AppInfo record);

    int updateByPrimaryKeyWithBLOBs(AppInfo record);

    int updateByPrimaryKey(AppInfo record);
}