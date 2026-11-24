package com.li.cloudpicturebackend.common;

import com.li.cloudpicturebackend.exception.ErrorCode;
import lombok.Data;

import java.io.Serializable;

/**
 * 全局响应封装类
 *
 * @param <T>
 */
@Data
public class BaseResponse<T> implements Serializable {
    //状态码
    private int code;

    //返回前端的数据
    private T data;

    //信息
    private String message;

    public BaseResponse(int code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    public BaseResponse(int code, T data) {
        this(code, data, "");
    }

    public BaseResponse(ErrorCode errorCode) {
        this(errorCode.getCode(), null, errorCode.getMessage());
    }
}
