package com.nadia.openplatfrom.isv.account.enums;


import com.nadia.openplatform.common.enumerate.EnumCode;

public enum IsvEnum implements EnumCode {
    ENABLE("ENABLE")
    ,DISABLE("DISABLE")
    ;

    private String code;

    IsvEnum(String code){
        this.code = code;
    }

    @Override
    public String getCode() {
        return this.code;
    }
}
