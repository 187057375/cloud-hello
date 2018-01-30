package com.mycompany.cloud.controller.test.shiro.sevice.impl;

import com.mycompany.cloud.controller.test.shiro.sevice.UserInfoService;
import com.mycompany.cloud.controller.test.shiro.dao.UserInfoDao;
import com.mycompany.cloud.controller.test.shiro.entity.UserInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserInfoServiceImpl implements UserInfoService {
    @Resource
    private UserInfoDao userInfoDao;
    @Override
    public UserInfo findByUsername(String username) {
        System.out.println("UserInfoServiceImpl.findByUsername()");
        return userInfoDao.findByUsername(username);
    }
}