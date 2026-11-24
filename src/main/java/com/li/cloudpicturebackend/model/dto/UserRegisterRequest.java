package com.li.cloudpicturebackend.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户注册请求对象
 */
@Data
public class UserRegisterRequest implements Serializable {
    /**
     * 使用UID检查序列化前后是否一致
     */
    private static final long serialVersionUID = 8545622010068370175L;

    /**
     * 账号
     */
    private String userAccount;

    /**
     * 用户密码
     */
    private String userPassword;

    /**
     * 确认密码
     */
    private String checkPassword;


}
