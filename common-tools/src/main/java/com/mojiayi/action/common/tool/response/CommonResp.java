package com.mojiayi.action.common.tool.response;

import cn.hutool.http.HttpStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 统一返回结果集
 *
 * @param <T>
 * @author mojiayi
 */
@ApiModel(value = "统一返回结果集")
public class CommonResp<T> implements Serializable {
    @ApiModelProperty(name = "data", value = "实际业务数据")
    private T data;
    @ApiModelProperty(name = "message", value = "接口调用的结果说明")
    private String message;
    @ApiModelProperty(name = "code", value = "接口调用的结果状态码，使用http请求的通用状态码，200表示成功，其他表示不成功")
    private Integer code;

    private CommonResp() {
    }

    private CommonResp(T data, String message, Integer code) {
        this.data = data;
        this.message = message;
        this.code = code;
    }

    public static <T> CommonResp<T> success() {
        return error(HttpStatus.HTTP_OK, "Success");
    }

    public static <T> CommonResp<T> success(String message) {
        return error(HttpStatus.HTTP_OK, message);
    }

    public static <T> CommonResp<T> success(T data) {
        return error(HttpStatus.HTTP_OK, "Success", data);
    }

    public static <T> CommonResp<T> success(String message, T data) {
        return error(HttpStatus.HTTP_OK, message, data);
    }

    public static <T> CommonResp<T> error(Integer code, String message) {
        return error(code, message, null);
    }

    public static <T> CommonResp<T> error(Integer code, String message, T data) {
        return new CommonResp<T>(data, message, code);
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
