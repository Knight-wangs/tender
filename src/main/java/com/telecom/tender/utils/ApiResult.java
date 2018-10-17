package com.telecom.tender.utils;

public class ApiResult<T> {
    public Integer code;
    public String msg;
    public T data;

    public ApiResult success(){
        this.code = ResultCode.SUCCESS.getCode();
        this.msg = ResultCode.SUCCESS.getDesc();
        return this;
    }

    public ApiResult success(T data){
        this.code = ResultCode.SUCCESS.getCode();
        this.msg = ResultCode.SUCCESS.getDesc();
        this.data = data;
        return this;
    }

    public ApiResult failure(){
        this.code = ResultCode.ERROR.getCode();
        this.msg = ResultCode.ERROR.getDesc();
        return this;
    }

    public ApiResult failure(T data){
        this.code = ResultCode.ERROR.getCode();
        this.msg = ResultCode.ERROR.getDesc();
        this.data = data;
        return this;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}