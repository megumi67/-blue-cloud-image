package com.li.cloudpicturebackend.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.li.cloudpicturebackend.constant.UserConstant;
import com.li.cloudpicturebackend.exception.BusinessException;
import com.li.cloudpicturebackend.exception.ErrorCode;
import com.li.cloudpicturebackend.model.entity.User;
import com.li.cloudpicturebackend.model.enums.UserRoleEnum;
import com.li.cloudpicturebackend.model.vo.LoginUserVO;
import com.li.cloudpicturebackend.service.UserService;
import com.li.cloudpicturebackend.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * @author lizx
 * @description 针对表【user(用户)】的数据库操作Service实现
 * @createDate 2026-11-20 16:38:57
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    /**
     * 用户注册
     *
     * @param userAccount   账号
     * @param userPassword  密码
     * @param checkPassword 再次确认
     * @return
     */
    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {
        //1.校验参数
        if (StrUtil.hasBlank(userAccount, userPassword, checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号过短");
        }
        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户密码过短");
        }
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户两次输入的密码不一致");
        }
        //2.检查账号是否与数据库中已有的重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        Long count = this.baseMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户已存在");
        }
        //3.密码一定要加密
        String encryptPassword = getEncryptPassword(userPassword);
        //4.插入数据到数据库中
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        user.setUserName("无名");
        user.setUserRole(UserRoleEnum.USER.getValue());
        boolean save = this.save(user);
        if (!save) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "注册失败，数据库错误");
        }
        return user.getId();
    }


    /**
     * 密码加密
     *
     * @param password
     * @return
     */
    @Override
    public String getEncryptPassword(String password) {
        //加盐
        final String SALT = "secretPassword";
        return DigestUtils.md5DigestAsHex((SALT + password).getBytes());
    }

    /**
     * 用户登录
     *
     * @param userAccount
     * @param userPassword
     * @param request
     * @return
     */
    @Override
    public LoginUserVO userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        //1.校验
        if (StrUtil.hasBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号错误");
        }
        if (userPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户密码错误");
        }
        //2.对用户输入的密码进行加密
        String encryptPassword = getEncryptPassword(userPassword);
        //3.查询数据库，检查用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("userPassword", encryptPassword);
        User user = this.baseMapper.selectOne(queryWrapper);
        //不存在，抛异常
        if (user == null) {
            log.info("Failed to login,userAccount is mot exist or password cannot match. ");
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户不存在或者密码错误");
        }

        //4.保存用户登录状态到session中
        request.getSession().setAttribute(UserConstant.USER_LOGIN_STATUS, user);
        //5.返回用户数据
        return this.getLoginUserVO(user);
    }


    /**
     * 获取当前登录用户
     *
     * @param request
     * @return
     */
    @Override
    public User getLoginUser(HttpServletRequest request) {
        //判断是否已经登录
        Object UserObj = request.getSession().getAttribute(UserConstant.USER_LOGIN_STATUS);
        User currentUser = (User) UserObj;
        if (currentUser == null || currentUser.getId() == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        //为保证数据实时性，再次查询数据库数据(若追求性能可直接返回上述的对象)
        Long userId = currentUser.getId();
        currentUser = this.getById(userId);
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        return currentUser;
    }


    /**
     * 获取脱敏类的用户信息
     *
     * @param user
     * @return
     */
    @Override
    public LoginUserVO getLoginUserVO(User user) {
        if (user == null) {
            return null;
        }
        LoginUserVO loginUserVO = new LoginUserVO();
        BeanUtil.copyProperties(user, loginUserVO);
        return loginUserVO;
    }


    /**
     * 用户登出
     *
     * @param request
     * @return
     */
    @Override
    public boolean userLogout(HttpServletRequest request) {
        //判断是否已经登录
        Object UserObj = request.getSession().getAttribute(UserConstant.USER_LOGIN_STATUS);
        if (UserObj == null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "未登录");
        }
        request.getSession().removeAttribute(UserConstant.USER_LOGIN_STATUS);
        return true;
    }
}




