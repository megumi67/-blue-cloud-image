package com.li.cloudpicturebackend.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.io.StreamTokenizer;

/**
 * 用户注册请求对象
 */
@Data
public class UserRegisterRequest implements Serializable {

    private static final long serialVersionUID = 8545622010068370175L;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户密码
     */
    private String userPassword;

    /**
     * 确认密码
     */
    private String checkPassword;


}
