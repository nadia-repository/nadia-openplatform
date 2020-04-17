package com.nadia.openplatfrom.isv.manage.dao;

import com.nadia.openplatfrom.isv.manage.domain.MenuInfo;
import com.nadia.openplatfrom.isv.manage.domain.criteria.MenuInfoCriteria;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuInfoMapper {
    long countByExample(MenuInfoCriteria example);

    int deleteByExample(MenuInfoCriteria example);

    int deleteByPrimaryKey(Long id);

    int insert(MenuInfo record);

    int insertSelective(MenuInfo record);

    List<MenuInfo> selectByExample(MenuInfoCriteria example);

    MenuInfo selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") MenuInfo record, @Param("example") MenuInfoCriteria example);

    int updateByExample(@Param("record") MenuInfo record, @Param("example") MenuInfoCriteria example);

    int updateByPrimaryKeySelective(MenuInfo record);

    int updateByPrimaryKey(MenuInfo record);
}