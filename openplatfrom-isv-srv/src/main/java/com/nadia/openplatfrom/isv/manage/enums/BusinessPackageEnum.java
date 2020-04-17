package com.nadia.openplatfrom.isv.manage.enums;


import com.nadia.openplatform.common.enumerate.EnumCode;

public enum BusinessPackageEnum implements EnumCode {
    ENABLE("ENABLE")
    ,DISABLE("DISABLE")
    ;

    private String code;

    BusinessPackageEnum(String code){
        this.code = code;
    }

    @Override
    public String getCode() {
        return this.code;
    }
}
