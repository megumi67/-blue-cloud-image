package com.li.cloudpicturebackend.service;

import com.li.cloudpicturebackend.model.entity.User;
import com.baomidou.mybatisplus.spring.service.IService;
import com.li.cloudpicturebackend.model.vo.LoginUserVO;

import javax.servlet.http.HttpServletRequest;

/**
 * @author lizx
 * @description 针对表【user(用户)】的数据库操作Service
 * @createDate 2026-11-20 16:38:57
 */
public interface UserService extends IService<User> {
    /**
     * 用户注册
     *
     * @param userAccount   账号
     * @param userPassword  密码
     * @param checkPassword 再次确认
     * @return
     */
    long userRegister(String userAccount, String userPassword, String checkPassword);

    /**
     * 获取加密后的密码
     *
     * @param password
     * @return
     */
    String getEncryptPassword(String password);


    /**
     * 用户登录
     *
     * @param userAccount  账号
     * @param userPassword 密码
     * @return 返回脱敏后的数据
     */
    LoginUserVO userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 获取当前登录用户
     *
     * @param request
     * @return
     */
    User getLoginUser(HttpServletRequest request);

    /**
     * 获取脱敏类的用户信息
     *
     * @param user
     * @return
     */
    LoginUserVO getLoginUserVO(User user);


    /**
     * 用户登出
     *
     * @param request
     * @return
     */
    boolean userLogout(HttpServletRequest request);
}
