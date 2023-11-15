package com.zhangz.springbootdemocloudalibabacommon.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author 100451
 */
@Data
public class Result implements Serializable {
    private final static String SUCCESS = "200";
    private final static String ERROR_500 = "500";

    private String code;
    private String message;

    public Result(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public static Result success() {
        return new Result(SUCCESS, null);
    }

    public static Result success(String message) {
        return new Result(SUCCESS, message);
    }


    public static Result error(String message) {
        return new Result(ERROR_500, message);
    }
}
