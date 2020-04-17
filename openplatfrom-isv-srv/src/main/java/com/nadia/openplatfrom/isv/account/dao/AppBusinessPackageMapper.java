package com.nadia.openplatfrom.isv.account.dao;


import com.nadia.openplatfrom.isv.account.domain.AppBusinessPackage;
import com.nadia.openplatfrom.isv.account.domain.criteria.AppBusinessPackageCriteria;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AppBusinessPackageMapper {
    long countByExample(AppBusinessPackageCriteria example);

    int deleteByExample(AppBusinessPackageCriteria example);

    int deleteByPrimaryKey(Long id);

    int insert(AppBusinessPackage record);

    int insertSelective(AppBusinessPackage record);

    List<AppBusinessPackage> selectByExample(AppBusinessPackageCriteria example);

    AppBusinessPackage selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") AppBusinessPackage record, @Param("example") AppBusinessPackageCriteria example);

    int updateByExample(@Param("record") AppBusinessPackage record, @Param("example") AppBusinessPackageCriteria example);

    int updateByPrimaryKeySelective(AppBusinessPackage record);

    int updateByPrimaryKey(AppBusinessPackage record);
}