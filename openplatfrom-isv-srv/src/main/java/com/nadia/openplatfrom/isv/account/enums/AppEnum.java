package com.nadia.openplatfrom.isv.account.enums;


import com.nadia.openplatform.common.enumerate.EnumCode;

public enum AppEnum implements EnumCode {
    ENABLE("ENABLE")
    ,DISABLE("DISABLE")
    ;

    private String code;

    AppEnum(String code){
        this.code = code;
    }

    @Override
    public String getCode() {
        return this.code;
    }
}
