package com.yupi.my_usercenter_backend.constant;

public interface UserConstant {

    // 用户登录态键
    static final String USER_LOGIN_STATE = "userLoginState";

    // 盐值，用于加密增加密码复杂度
    String SALT_VALUE = "ssssSaltPoiseValeAaa";

    // 默认权限
    int DEFAULT_ROLE = 0;

    // 特殊权限
    int ADMIN_ROLE = 1;
}
