package com.nadia.openplatfrom.isv.manage.enums;


import com.nadia.openplatform.common.enumerate.EnumCode;

public enum ApiEnum implements EnumCode {
    ENABLE("ENABLE")
    ,DISABLE("DISABLE")
    ;

    private String code;

    ApiEnum(String code){
        this.code = code;
    }

    @Override
    public String getCode() {
        return this.code;
    }
}
