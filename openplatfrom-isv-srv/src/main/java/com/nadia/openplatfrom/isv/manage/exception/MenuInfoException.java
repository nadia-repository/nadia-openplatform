package com.nadia.openplatfrom.isv.manage.exception;


import com.nadia.openplatform.common.exception.BaseException;

public class MenuInfoException extends BaseException {
    public MenuInfoException(String message){
        super(message);
    }

    public MenuInfoException(Long errorCode){
        this("Menu Info exception");
        setErrorCode(errorCode);
    }

    public MenuInfoException(Long errorCode, Object[] args){
        super(errorCode,args);
    }
}
