package com.li.cloudpicturebackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.li.cloudpicturebackend.model.entity.User;
import com.li.cloudpicturebackend.service.UserService;
import com.li.cloudpicturebackend.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
* @author lizx
* @description 针对表【user(用户)】的数据库操作Service实现
* @createDate 2026-11-20 16:38:57
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

}




