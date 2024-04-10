package com.yupi.my_usercenter_backend.service.impl;
import java.util.Date;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yupi.my_usercenter_backend.common.ErrorCode;
import com.yupi.my_usercenter_backend.common.ResultUtils;
import com.yupi.my_usercenter_backend.exception.BusinessException;
import com.yupi.my_usercenter_backend.mapper.UserinfoMapper;
import com.yupi.my_usercenter_backend.model.Userinfo;
import com.yupi.my_usercenter_backend.service.UserinfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.yupi.my_usercenter_backend.constant.UserConstant.SALT_VALUE;
import static com.yupi.my_usercenter_backend.constant.UserConstant.USER_LOGIN_STATE;


/**
* @author poise
* @description 针对表【userinfo(用户信息表)】的数据库操作Service实现
* @createDate 2024-04-07 17:11:57
*/
@Service
@Slf4j
public class UserinfoServiceImpl extends ServiceImpl<UserinfoMapper, Userinfo>
    implements UserinfoService {

    @Resource
    public UserinfoMapper userinfoMapper;
    /**
     * 用户注册
     * @param userAccount
     * @param userPassword
     * @param checkPassword
     * @return
     */
    @Override
    public Long UserRegister(String userAccount, String userPassword, String checkPassword) {
        // 1. 校验参数
        // 非空校验
        if (userAccount.isEmpty() || userPassword.isEmpty() || checkPassword.isEmpty()){
            throw new BusinessException(ErrorCode.NULL_ERROR, "请求参数为空");
        }
        // 账号长度校验
        if (userAccount.length() < 4){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号过短");
        }
        // 账号格式校验（使用正则检验,账号不能包括一下字符）
        String reg = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        // java中用来匹配正则的类Matcher; Pattern正则类
        Pattern p = Pattern.compile(reg);
        Matcher matcher = p.matcher(userAccount);
        if (matcher.find()){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户名不能包含特殊字符！！！");
        }
        // 账号不能重复检验
        // 构建Userinfo表的SQL查询工具类
        QueryWrapper<Userinfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        Long count = userinfoMapper.selectCount(queryWrapper);
        if (count != 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户名重复");
        }

        // 2. 密码校验
        if (userPassword.length() < 8 || checkPassword.length() < 8){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"密码长度不够");
        }
        if (!userPassword.equals(checkPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"两次密码不一致");
        }
        // 3. 密码加密
        // 用于计算字符串的MD5哈希值，并将其转换成16进制表示的字符串形式
        String md5Password = DigestUtils.md5DigestAsHex((SALT_VALUE + userPassword).getBytes(StandardCharsets.UTF_8));

        // 存储信息，注册成功
        Userinfo userinfo = new Userinfo();
        userinfo.setUserAccount(userAccount);
        userinfo.setUserPassword(md5Password);
//        int res = userinfoMapper.insert(userinfo);
        boolean res = this.save(userinfo);
        if (!res){
            return -1L;
        }

        return userinfo.getId();
    }

    /**
     * 用户登录
     * @param userAccount
     * @param userPassWord
     * @param request 获取请求的各种信息，比如请求的参数、请求头、请求的方法（GET、POST 等）、请求的 URI 等
     * @return
     */
    @Override
    public Userinfo UserLogin(String userAccount, String userPassWord, HttpServletRequest request) {
        // 1. 校验
        if (StringUtils.isAnyEmpty(userAccount,userPassWord)){
            throw new BusinessException(ErrorCode.NULL_ERROR, "请求参数为空");
        }
        if (userAccount.length() < 4 || userPassWord.length() < 8){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号或密码长度不够");
        }
        String reg = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(userAccount);
        if (matcher.find()){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号不能包含特殊字符！");
        }
        // 2. 将密码用通用的盐值加密，然后与数据库中加密密码比较
        String loginMd5Password = DigestUtils.md5DigestAsHex((SALT_VALUE + userPassWord).getBytes(StandardCharsets.UTF_8));

        QueryWrapper<Userinfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("userPassword", loginMd5Password);

        Userinfo userinfo = userinfoMapper.selectOne(queryWrapper);
        if (userinfo == null){
            log.warn("user login failed, userAccount Cannot match studentPassword");
            return null;
        }

        // 3. 返回用户信息脱敏信息
        Userinfo user = getSafeUserinfo(userinfo);


        // 4. 记录用户登录态
        request.getSession().setAttribute(USER_LOGIN_STATE,user);

        return user;
    }

    /**
     * 用户脱敏
     * @param userinfo
     * @return
     */
    @Override
    public Userinfo getSafeUserinfo(Userinfo userinfo){
        if (userinfo == null){
            return null;
        }
        Userinfo user = new Userinfo();
        user.setUserName(userinfo.getUserName());
        user.setUserAccount(userinfo.getUserAccount());
        user.setUserAvatarUrl(userinfo.getUserAvatarUrl());
        user.setGender(userinfo.getGender());
        user.setPhone(userinfo.getPhone());
        user.setEmail(userinfo.getEmail());
        user.setUserStatus(userinfo.getUserStatus());
        user.setCreateTime(userinfo.getCreateTime());
        user.setUpdateTime(userinfo.getUpdateTime());
        return user;
    }

    /**
     * 退出登录
     * @param request
     * @return
     */
    @Override
    public int userLogout(HttpServletRequest request) {
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return 1;
    }


}




