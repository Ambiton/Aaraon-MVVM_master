package com.myzr.allproducts.utils;

/**
 * @author Areo
 * @description:
 * @date : 2020/1/1 22:13
 */
public class HttpStatus {
    /**
     * 返回成功
     */
    public static int STATUS_CODE_SUCESS = 200;
    /**
     * token过期，需要执行登录 old
     */
    public static int STATUS_CODE_TOKEN_OVERDUE_OLD = 401;

    /**
     * token到期，需要执行登录
     */
    public static int STATUS_CODE_TOKEN_OVERDUE = 901;

    /**
     * token失效，需要执行登录
     */
    public static int STATUS_CODE_TOKEN_NOUSE = 902;

    /**
     * token过期，需要执行登录
     */
    public static int STATUS_CODE_TOKEN_FORBIDDEN = 903;

    /**
     * 已被注册或验证码过期
     */
    public static int STATUS_CODE_OTHER_ERR = 500;
}
