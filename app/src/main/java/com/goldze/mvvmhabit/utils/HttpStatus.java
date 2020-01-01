package com.goldze.mvvmhabit.utils;

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
     * token过期，需要执行登录
     */
    public static int STATUS_CODE_TOKEN_OVERDUE = 401;
    /**
     * 已被注册或验证码过期
     */
    public static int STATUS_CODE_VERIFIED_ERR = 500;
}
