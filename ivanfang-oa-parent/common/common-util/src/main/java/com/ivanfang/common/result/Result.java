package com.ivanfang.common.result;

import lombok.Data;

@Data
public class Result<T> {

    private Integer code;   // status code
    private String message; // return message
    private T data;         // return data

    private Result() {
    }

    // encapsulate the data (result) that we want to return
    public static <T> Result<T> build(T data, ResultCodeEnum resultCodeEnum) {
        Result<T> result = new Result<>();

        result.setCode(resultCodeEnum.getCode());
        result.setMessage(resultCodeEnum.getMessage());
        if (data != null) {
            result.setData(data);
        }

        return result;
    }

    // success without data
    public static <T> Result<T> success() {
        return build(null, ResultCodeEnum.SUCCESS);
    }

    // success with data
    public static <T> Result<T> success(T data) {
        return build(data, ResultCodeEnum.SUCCESS);
    }

    // failed without data
    public static <T> Result<T> failed() {
        return build(null, ResultCodeEnum.FAILED);
    }

    // failed with data
    public static <T> Result<T> failed(T data) {
        return build(data, ResultCodeEnum.FAILED);
    }

    // login failed
    public static <T> Result<T> loginFailed() {
        return build(null, ResultCodeEnum.LOGIN_FAILED);
    }

    // modify the message
    public Result<T> message(String msg) {
        this.setMessage(msg);
        return this;
    }

    // modify the code
    public Result<T> code(Integer code) {
        this.setCode(code);
        return this;
    }

}
