package com.nadia.openplatform.common.exception;

import com.nadia.openplatform.common.rest.RestHeader;
import com.nadia.openplatform.common.utils.I18nUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Slf4j
abstract public class BaseException extends RuntimeException{

    final public static long DEFAULT_ERROR_CODE= 1;
    final public static long INVALID_CONVERSION_ERROR_CODE= 121;
    final public static long INVALID_ARGUMENTS_ERROR_CODE= 122;

    final public static long UNAUTHORIZED_ERROR_CODE= 403;
    final public static long SAME_LOGIN_ERROR_CODE= 402;
    final public static long UNAUTHORIZED_ERROR_LONG_CODE= 401;
//    final public static long NOTFOUND_ERROR_CODE= 404;
//    final public static long REQUEST_TIMEOUT_ERROR_CODE= 408;

    private Long errorCode;

    private Object[] args= {};

    private Object actionModel;

    public BaseException(){
        super();
    }

    public BaseException(String message){
        super(message);
    }

    public BaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public BaseException(Throwable cause) {
        super(cause);
    }

    public BaseException(long errorCode){
        this.errorCode= errorCode;
    }

    public BaseException(long errorCode, Object actionModel){
        this.errorCode= errorCode;
        this.actionModel= actionModel;
    }

    public BaseException(long errorCode, Throwable cause){
        super(cause);
        this.errorCode= errorCode;
    }

    public BaseException(long errorCode, Object actionModel, Throwable cause){
        super(cause);
        this.errorCode= errorCode;
        this.actionModel= actionModel;
    }

    public BaseException(long errorCode, Object[] args){
        this.errorCode= errorCode;
        this.args= args;
    }

    public BaseException(long errorCode, Object[] args, Object actionModel){
        this.errorCode= errorCode;
        this.args= args;
        this.actionModel= actionModel;
    }

    public BaseException(long errorCode, Object[] args, Throwable cause){
        super(cause);
        this.errorCode= errorCode;
        this.args= args;
    }
    public BaseException(long errorCode, Object[] args, Object actionModel, Throwable cause){
        super(cause);
        this.errorCode= errorCode;
        this.args= args;
        this.actionModel= actionModel;
    }

    protected RestHeader handleException(){
        RestHeader response= new RestHeader();
        if(errorCode!= null) {
            response.setErrorCode(errorCode);
            String message= I18nUtils.getMessage(String.valueOf(errorCode),args,getMessage());
            response.setMsg(message);
            return response;
        }
        response.setErrorCode(DEFAULT_ERROR_CODE);
        response.setMsg(getMessage());
        return response;
    }

    final public RestHeader getExceptionResponse(){
        RestHeader response= handleException();
        return response;
    }

    final public Object[] getArgs(){
        if(args!= null){
            Object[] newArgs= new Object[args.length];
            System.arraycopy(args,0,newArgs,0,args.length);
            return newArgs;
        }
        return null;
    }

}
