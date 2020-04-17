package com.nadia.openplatfrom.isv.account.dao;

import com.nadia.openplatfrom.isv.account.domain.IsvInfo;
import com.nadia.openplatfrom.isv.account.domain.criteria.IsvInfoCriteria;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IsvInfoMapper {
    long countByExample(IsvInfoCriteria example);

    int deleteByExample(IsvInfoCriteria example);

    int deleteByPrimaryKey(Long id);

    int insert(IsvInfo record);

    int insertSelective(IsvInfo record);

    List<IsvInfo> selectByExample(IsvInfoCriteria example);

    IsvInfo selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") IsvInfo record, @Param("example") IsvInfoCriteria example);

    int updateByExample(@Param("record") IsvInfo record, @Param("example") IsvInfoCriteria example);

    int updateByPrimaryKeySelective(IsvInfo record);

    int updateByPrimaryKey(IsvInfo record);
}