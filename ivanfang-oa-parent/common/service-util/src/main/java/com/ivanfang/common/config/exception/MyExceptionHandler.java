package com.ivanfang.common.config.exception;

import com.ivanfang.common.result.Result;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class MyExceptionHandler {

    // global exception handler
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result error(Exception e) {
        e.printStackTrace();
        return Result.failed().message("執行全局例外處理...");
    }

    // specific exception handler
    // if there is a specific exception handler, spring mvc will execute the specific one first
    @ExceptionHandler(ArithmeticException.class)
    @ResponseBody
    public Result error(ArithmeticException e) {
        e.printStackTrace();
        return Result.failed().message("執行數學運算例外處理...");
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseBody
    public Result error(AccessDeniedException e) {
        e.printStackTrace();
        return Result.failed().message("對不起，您沒有此權限");
    }

    // self-defined exception handler
    @ExceptionHandler(MyException.class)
    @ResponseBody
    public Result error(MyException e) {
        e.printStackTrace();
        return Result.failed().code(e.getCode()).message(e.getMessage());
    }

}
