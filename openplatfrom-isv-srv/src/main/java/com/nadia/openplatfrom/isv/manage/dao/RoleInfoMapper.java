package com.nadia.openplatfrom.isv.manage.dao;

import com.nadia.openplatfrom.isv.manage.domain.RoleInfo;
import com.nadia.openplatfrom.isv.manage.domain.criteria.RoleInfoCriteria;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleInfoMapper {
    long countByExample(RoleInfoCriteria example);

    int deleteByExample(RoleInfoCriteria example);

    int deleteByPrimaryKey(Long id);

    int insert(RoleInfo record);

    int insertSelective(RoleInfo record);

    List<RoleInfo> selectByExample(RoleInfoCriteria example);

    RoleInfo selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") RoleInfo record, @Param("example") RoleInfoCriteria example);

    int updateByExample(@Param("record") RoleInfo record, @Param("example") RoleInfoCriteria example);

    int updateByPrimaryKeySelective(RoleInfo record);

    int updateByPrimaryKey(RoleInfo record);
}