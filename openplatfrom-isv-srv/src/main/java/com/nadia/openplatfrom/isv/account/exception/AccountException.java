package com.nadia.openplatfrom.isv.account.exception;


import com.nadia.openplatform.common.exception.BaseException;

public class AccountException extends BaseException {
    public AccountException(String message){
        super(message);
    }

    public AccountException(Long errorCode){
        this("Account exception");
        setErrorCode(errorCode);
    }

    public AccountException(Long errorCode, Object[] args){
        super(errorCode,args);
    }
}
