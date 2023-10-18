package com.zhangz.springbootdemocommon.exception;

/**
 * <br>
 * <b>功能：</b>SystemException<br>
 * <b>作者：</b>zhaohao<br>
 * <b>日期：</b>2018/10/24 <br>
 * <b>版权所有： 2018，博思票据云<br>
 */
public class SystemException extends Exception {
    private String code; //错误编码
    private String msg; //错误详情

    public SystemException(String code, String msg) {
        super(code);
        this.code = code;
        this.msg = msg;
    }

    public SystemException(String message, String code, String msg) {
        super(message);
        this.code = code;
        this.msg = msg;
    }

    public SystemException(String message, Throwable cause, String code, String msg) {
        super(message, cause);
        this.code = code;
        this.msg = msg;
    }

    public SystemException(Throwable cause, String code, String msg) {
        super(cause);
        this.code = code;
        this.msg = msg;
    }

    public SystemException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, String code, String msg) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
