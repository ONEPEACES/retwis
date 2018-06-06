package com.example.common.resp;

import com.example.common.respcode.ResponseCode;

public class RestResponse<T> {
    private int code;
    private T data;
    private String message;

    private RestResponse(int code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    private RestResponse(int code, T data) {
        this.code = code;
        this.data = data;
    }

    private RestResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private RestResponse(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public T getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public static <T> RestResponse<T> createBySuccMessage(String message) {
        return new RestResponse(ResponseCode.SUCC.getCode(), message);
    }

    public static <T> RestResponse<T> createBySucc() {
        return new RestResponse<>(ResponseCode.SUCC.getCode());
    }

    public static <T> RestResponse<T> createBySucc(T data) {
        return new RestResponse<>(ResponseCode.SUCC.getCode(), data);
    }

    public static <T> RestResponse<T> createBySucc(T data, String msg) {
        return new RestResponse<>(ResponseCode.SUCC.getCode(), data, msg);
    }

    public static <T> RestResponse<T> createByErrorMessage(String message) {
        return new RestResponse(ResponseCode.ERROR.getCode(), message);
    }

    public static <T> RestResponse<T> createByError() {
        return new RestResponse<>(ResponseCode.ERROR.getCode());
    }


}
