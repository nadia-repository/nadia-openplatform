package com.nadia.openplatfrom.isv.manage.dao;

import com.nadia.openplatfrom.isv.manage.domain.RoleMenu;
import com.nadia.openplatfrom.isv.manage.domain.criteria.RoleMenuCriteria;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleMenuMapper {
    long countByExample(RoleMenuCriteria example);

    int deleteByExample(RoleMenuCriteria example);

    int deleteByPrimaryKey(Long id);

    int insert(RoleMenu record);

    int insertSelective(RoleMenu record);

    List<RoleMenu> selectByExample(RoleMenuCriteria example);

    RoleMenu selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") RoleMenu record, @Param("example") RoleMenuCriteria example);

    int updateByExample(@Param("record") RoleMenu record, @Param("example") RoleMenuCriteria example);

    int updateByPrimaryKeySelective(RoleMenu record);

    int updateByPrimaryKey(RoleMenu record);
}