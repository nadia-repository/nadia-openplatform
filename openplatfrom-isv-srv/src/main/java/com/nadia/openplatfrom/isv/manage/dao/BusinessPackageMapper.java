package com.nadia.openplatfrom.isv.manage.dao;

import com.nadia.openplatfrom.isv.manage.domain.BusinessPackage;
import com.nadia.openplatfrom.isv.manage.domain.criteria.BusinessPackageCriteria;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BusinessPackageMapper {
    long countByExample(BusinessPackageCriteria example);

    int deleteByExample(BusinessPackageCriteria example);

    int deleteByPrimaryKey(Long id);

    int insert(BusinessPackage record);

    int insertSelective(BusinessPackage record);

    List<BusinessPackage> selectByExample(BusinessPackageCriteria example);

    BusinessPackage selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") BusinessPackage record, @Param("example") BusinessPackageCriteria example);

    int updateByExample(@Param("record") BusinessPackage record, @Param("example") BusinessPackageCriteria example);

    int updateByPrimaryKeySelective(BusinessPackage record);

    int updateByPrimaryKey(BusinessPackage record);
}