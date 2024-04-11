package com.yupi.my_usercenter_backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yupi.my_usercenter_backend.model.Userinfo;

import javax.servlet.http.HttpServletRequest;


/**
* @author poise
* @description 针对表【userinfo(用户信息表)】的数据库操作Service
* @createDate 2024-04-07 17:11:57
*/
public interface UserinfoService extends IService<Userinfo> {

    /**
     * 用户注册
     * @param userAccount
     * @param userPassword
     * @param checkPassword
     * @return
     */
    Long UserRegister(String userAccount, String userPassword, String checkPassword);

    /**
     * 用户登录
     * @param userAccount
     * @param
     * @param request 获取请求的各种信息，比如请求的参数、请求头、请求的方法（GET、POST 等）、请求的 URI 等
     * @return
     */
    Userinfo UserLogin(String userAccount, String userPassWord, HttpServletRequest request);

    /**
     * 用户脱敏
     * @param userinfo
     * @return
     */
    Userinfo getSafeUserinfo(Userinfo userinfo);

    /**
     * 用户注销
     * @param request
     * @return
     */
    int userLogout(HttpServletRequest request);

    /**
     * 获取当前用户
     * @param request
     * @return
     */
    Userinfo getLoginUser(HttpServletRequest request);

}
