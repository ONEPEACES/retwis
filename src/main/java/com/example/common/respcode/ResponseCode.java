package com.example.common.respcode;

public enum  ResponseCode {
    SUCC(200,"SUCC"),ERROR(500,"ERROR");

    private final int code;
    private final String msg;

    ResponseCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
