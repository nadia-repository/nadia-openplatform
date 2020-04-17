package com.nadia.openplatfrom.isv.account.dao;

import com.nadia.openplatfrom.isv.account.domain.LoginHistories;
import com.nadia.openplatfrom.isv.account.domain.criteria.LoginHistoriesCriteria;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoginHistoriesMapper {
    long countByExample(LoginHistoriesCriteria example);

    int deleteByExample(LoginHistoriesCriteria example);

    int deleteByPrimaryKey(Long id);

    int insert(LoginHistories record);

    int insertSelective(LoginHistories record);

    List<LoginHistories> selectByExample(LoginHistoriesCriteria example);

    LoginHistories selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") LoginHistories record, @Param("example") LoginHistoriesCriteria example);

    int updateByExample(@Param("record") LoginHistories record, @Param("example") LoginHistoriesCriteria example);

    int updateByPrimaryKeySelective(LoginHistories record);

    int updateByPrimaryKey(LoginHistories record);
}