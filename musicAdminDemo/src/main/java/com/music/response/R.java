package com.music.response;

import lombok.Data;

/**
 * 接口类型返回定义
 */
@Data
public class R {

    public static final int CODE_SUCCESS = 200;
    public static final int CODE_FAILED = 400;
    public static final int CODE_NOT_LOGIN = 401;
    public static final int CODE_ADMIN_LOGIN = 201;


    //是否成功
    private boolean success;
    //状态码
    private int code;
    //描述
    private String msg;
    //数据
    private Object data;


    /**
     * 初始化一个新创建的 AjaxResult 对象，使其表示一个空消息。
     */
    public R()
    {
    }

    //提供静态方法，快速创建返回对象
    public static R SUCCESS(String msg) {
        R r = new R();
        r.success = true;
        r.code = CODE_SUCCESS;
        r.msg = msg;
        return r;
    }

    public static R SUCCESS(String msg, Object data) {
        R success = SUCCESS(msg);
        success.data = data;
        return success;
    }

    public static R FAILED(String msg) {
        R r = new R();
        r.success = true;
        r.code = CODE_FAILED;
        r.msg = msg;
        return r;
    }

    public static R FAILED(String msg, Object data) {
        R failed = FAILED(msg);
        failed.data = data;
        return failed;
    }

    public static R NOT_LOGIN() {
        R login = FAILED("用户未登录!");
        login.code = CODE_NOT_LOGIN;
        return login;
    }


    public static R ADMIN_LOGIN( Object data) {
        R login = SUCCESS("管理员账号登录!");
        login.code = CODE_ADMIN_LOGIN;
        login.data = data;
        return login;
    }


    public R setData(Object data) {
        this.data = data;
        return this;
    }

}