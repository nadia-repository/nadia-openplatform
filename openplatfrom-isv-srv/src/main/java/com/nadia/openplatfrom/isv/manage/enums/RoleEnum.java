package com.nadia.openplatfrom.isv.manage.enums;


import com.nadia.openplatform.common.enumerate.EnumCode;

public enum RoleEnum implements EnumCode {
    ADMIN("1","admin")
    ,ISV("2","Independent Software Vendors")
    ;

    private String code;
    private String name;

    RoleEnum(String code,String name){
        this.code = code;
        this.name = name;
    }

    @Override
    public String getCode() {
        return this.code;
    }
}
