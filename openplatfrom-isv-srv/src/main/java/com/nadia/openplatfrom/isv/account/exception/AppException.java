package com.nadia.openplatfrom.isv.account.exception;


import com.nadia.openplatform.common.exception.BaseException;

public class AppException extends BaseException {
    public AppException(String message){
        super(message);
    }

    public AppException(Long errorCode){
        this("App exception");
        setErrorCode(errorCode);
    }

    public AppException(Long errorCode, Object[] args){
        super(errorCode,args);
    }
}
