package com.nadia.openplatfrom.isv.manage.dao;

import com.nadia.openplatfrom.isv.manage.domain.ApiInfo;
import com.nadia.openplatfrom.isv.manage.domain.criteria.ApiInfoCriteria;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ApiInfoMapper {
    long countByExample(ApiInfoCriteria example);

    int deleteByExample(ApiInfoCriteria example);

    int deleteByPrimaryKey(Long id);

    int insert(ApiInfo record);

    int insertSelective(ApiInfo record);

    List<ApiInfo> selectByExample(ApiInfoCriteria example);

    ApiInfo selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") ApiInfo record, @Param("example") ApiInfoCriteria example);

    int updateByExample(@Param("record") ApiInfo record, @Param("example") ApiInfoCriteria example);

    int updateByPrimaryKeySelective(ApiInfo record);

    int updateByPrimaryKey(ApiInfo record);
}