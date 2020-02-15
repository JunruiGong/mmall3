package com.mmall.common;

/**
 * @Author: Junrui Gong
 * @Date: 2/14/20
 */
public enum ResponseCode {

    SUCCESS(0, "Success"),
    ERROR(1, "Error"),
    NEED_LOGIN(10, "Need Login"),
    ILLEGAL_ARGUMENT(2, "Illegal Argument");

    private final int code;
    private final String desc;

    ResponseCode(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }


    public int getCode(){
        return code;
    }

    public String getDesc(){
        return desc;
    }
}
