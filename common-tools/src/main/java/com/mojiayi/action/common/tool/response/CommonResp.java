package com.mojiayi.action.common.tool.response;

import cn.hutool.http.HttpStatus;

import java.io.Serializable;

/**
 * 统一返回结果集
 *
 * @param <T>
 * @author liguangri
 */
public class CommonResp<T> implements Serializable {
    private T data;
    private String message;
    /**
     * 使用http请求的通用状态码，200表示成功，其他表示不成功
     */
    private Integer code;

    public CommonResp() {
    }

    public CommonResp(T data, String message, Integer code) {
        this.data = data;
        this.message = message;
        this.code = code;
    }

    public CommonResp(String message, Integer code) {
        this(null, message, code);
    }

    public CommonResp(T data) {
        this(data, "Success", HttpStatus.HTTP_OK);
    }

    public boolean isSuccess() {
        return HttpStatus.HTTP_OK == this.code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
