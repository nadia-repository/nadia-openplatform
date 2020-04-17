package com.nadia.openplatfrom.isv.manage.exception;


import com.nadia.openplatform.common.exception.BaseException;

public class BusinessPackageException extends BaseException {
    public BusinessPackageException(String message){
        super(message);
    }

    public BusinessPackageException(Long errorCode){
        this("BusinessPackage exception");
        setErrorCode(errorCode);
    }

    public BusinessPackageException(Long errorCode, Object[] args){
        super(errorCode,args);
    }
}
