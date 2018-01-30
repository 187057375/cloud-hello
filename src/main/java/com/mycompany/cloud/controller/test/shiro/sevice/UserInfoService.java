package com.mycompany.cloud.controller.test.shiro.sevice;

import com.mycompany.cloud.controller.test.shiro.entity.UserInfo;

public interface UserInfoService {
    /**通过username查找用户信息;*/
    public UserInfo findByUsername(String username);
}