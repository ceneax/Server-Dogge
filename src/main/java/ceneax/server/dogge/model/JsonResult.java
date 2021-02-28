package ceneax.server.dogge.model;

import ceneax.server.dogge.Code;

/**
 * Description: 基类 服务端返回结果 实体类
 * Author: ceneax
 * Website: ceneax.com
 * Date: 2021/2/28 20:16
 */
public class JsonResult<T> {

    // 状态码
    private int code;
    // 描述内容
    private String msg;
    // 返回数据
    private T data;

    /**
     * 请求成功，没有描述内容和数据返回
     */
    public JsonResult() {
        this(null);
    }

    /**
     * 请求成功，没有描述内容，有数据返回
     */
    public JsonResult(T data) {
        this("", data);
    }

    /**
     * 请求成功，有描述内容和数据返回
     */
    public JsonResult(String msg, T data) {
        this(Code.RESPONSE_CODE_OK, msg, data);
    }

    /**
     * 请求失败，没有描述内容
     */
    public JsonResult(int code) {
        this(code, "");
    }

    /**
     * 请求失败，有描述内容
     */
    public JsonResult(int code, String msg) {
        this(code, msg, null);
    }

    public JsonResult(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
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
