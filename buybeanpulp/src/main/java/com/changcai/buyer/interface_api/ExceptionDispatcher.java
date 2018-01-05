package com.changcai.buyer.interface_api;

import com.changcai.buyer.okhttp.NetErrorException;
import com.google.gson.JsonParseException;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.text.ParseException;
import java.util.concurrent.TimeoutException;

import retrofit2.adapter.rxjava.HttpException;

/**
 * Created by liuxingwei on 2017/2/24.
 */

public class ExceptionDispatcher {

    private static final int UNAUTHORIZED = 401;
    private static final int FORBIDDEN = 403;
    private static final int NOT_FOUND = 404;
    private static final int REQUEST_TIMEOUT = 408;
    private static final int INTERNAL_SERVER_ERROR = 500;
    private static final int BAD_GATEWAY = 502;
    private static final int SERVICE_UNAVAILABLE = 503;
    private static final int GATEWAY_TIMEOUT = 504;

    public static Throwable handleException(Throwable e){
        ApiCodeErrorException ex;
        if (e instanceof HttpException){             //HTTP错误
            HttpException httpException = (HttpException) e;
            switch(httpException.code()){
                case UNAUTHORIZED:
                case FORBIDDEN:
                case NOT_FOUND:
                case REQUEST_TIMEOUT:
                case GATEWAY_TIMEOUT:
                case INTERNAL_SERVER_ERROR:
                case BAD_GATEWAY:
                case SERVICE_UNAVAILABLE:
                default:
                    ex = new ApiCodeErrorException(ErrorCode.NET_ERROR);//均视为网络错误
                    break;
            }
            return ex;
        }  else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException){
            ex = new ApiCodeErrorException(ErrorCode.SERVER_ERROR);
            return ex;
        }else if(e instanceof  ConnectException
                || e instanceof SocketTimeoutException
                || e instanceof TimeoutException){
            ex = new ApiCodeErrorException(ErrorCode.NET_ERROR);
            return ex;
        }else if (e instanceof  ApiCodeErrorException){
            ex = (ApiCodeErrorException) e;
            return ex;
        }else if (e instanceof  java.lang.IllegalArgumentException){
            return new ApiCodeErrorException(ErrorCode.IllegalArgumentException);
        }else{
            return  e;
        }
    }

    /**
     * 约定异常
     */



    public static Throwable handleNetException(Throwable e){
        NetErrorException ex;
        if (e instanceof HttpException){             //HTTP错误
            HttpException httpException = (HttpException) e;
            switch(httpException.code()){
                case UNAUTHORIZED:
                case FORBIDDEN:
                case NOT_FOUND:
                case REQUEST_TIMEOUT:
                case GATEWAY_TIMEOUT:
                case INTERNAL_SERVER_ERROR:
                case BAD_GATEWAY:
                case SERVICE_UNAVAILABLE:
                default:
                    ex = new NetErrorException(ErrorCode.NET_ERROR);//均视为网络错误
                    break;
            }
            return ex;
        }  else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException){
            ex = new NetErrorException(ErrorCode.SERVER_ERROR);
            return ex;
        }else if(e instanceof  ConnectException
                || e instanceof SocketTimeoutException
                || e instanceof TimeoutException){
            ex = new NetErrorException(ErrorCode.NET_ERROR);
            return ex;
        }else if (e instanceof  java.lang.IllegalArgumentException){
            return new ApiCodeErrorException(ErrorCode.IllegalArgumentException);
        }else{
            return  e;
        }
    }

}
