package com.yupi.my_usercenter_backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yupi.my_usercenter_backend.common.ErrorCode;
import com.yupi.my_usercenter_backend.exception.BusinessException;
import com.yupi.my_usercenter_backend.mapper.UserinfoMapper;
import com.yupi.my_usercenter_backend.model.Userinfo;
import com.yupi.my_usercenter_backend.model.dto.UserAddRequest;
import com.yupi.my_usercenter_backend.model.dto.UserQueryRequest;
import com.yupi.my_usercenter_backend.model.dto.UserUpdateRequest;
import com.yupi.my_usercenter_backend.service.UserManageService;
import com.yupi.my_usercenter_backend.service.UserinfoService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@Service
public class UserManageServiceImpl extends ServiceImpl<UserinfoMapper, Userinfo> implements UserManageService {

    @Resource
    public UserinfoMapper userinfoMapper;

    @Resource
    public UserinfoService userinfoService;

    @Override
    @Transactional
    public Long addUserRequest(UserAddRequest userAddRequest, HttpServletRequest request) {
        // 1. 校验
        if (userAddRequest.getUserAccount().length() < 4){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号不能小于4");
        }
        // 2. 赋值
        Userinfo newUser = new Userinfo();
        // 复制属性
        BeanUtils.copyProperties(userAddRequest, newUser);

        // 定义时间格式
        Date currentTime = new Date();
        newUser.setCreateTime(currentTime);
        newUser.setUpdateTime(currentTime);

        boolean res = this.save(newUser);
        if (!res){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "系统内部异常，操作失败");
        }

        return newUser.getId();
    }

    @Override
    public boolean addUserListRequest(List<Userinfo> userinfoList) {
        userinfoService.saveBatch(userinfoList);
        for (Userinfo u: userinfoList){
            userinfoService.getSafeUserinfo(u);
        }
        return true;
    }

    @Override
    public Long updateUserRequest(UserUpdateRequest userUpdateRequest, HttpServletRequest request) {
        // 1. 校验
        if (userUpdateRequest.getUserAccount().length() < 4.0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号不能小于4");
        }
        // 2. 拿到要修改的数据
        long updateId = userUpdateRequest.getId();
        QueryWrapper<Userinfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", updateId);
        // 执行查询操作
        Userinfo userToUpdate = this.getOne(queryWrapper);
        if (userToUpdate == null) {
            throw new BusinessException(ErrorCode.NullDate_ERROR, "未找到要更新的用户");
        }
        // 更新用户信息，只更新非空属性
        if (userUpdateRequest.getUserName() != null) {
            userToUpdate.setUserName(userUpdateRequest.getUserName());
        }
        if (userUpdateRequest.getUserAccount() != null) {
            userToUpdate.setUserAccount(userUpdateRequest.getUserAccount());
        }
        if (userUpdateRequest.getUserAvatarUrl() != null) {
            userToUpdate.setUserAvatarUrl(userUpdateRequest.getUserAvatarUrl());
        }
        if (userUpdateRequest.getGender() != null) {
            userToUpdate.setGender(userUpdateRequest.getGender());
        }
        if (userUpdateRequest.getPhone() != null) {
            userToUpdate.setPhone(userUpdateRequest.getPhone());
        }
        if (userUpdateRequest.getEmail() != null) {
            userToUpdate.setEmail(userUpdateRequest.getEmail());
        }
        if (userUpdateRequest.getUserStatus() != null) {
            userToUpdate.setUserStatus(userUpdateRequest.getUserStatus());
        }
        if (userUpdateRequest.getUserRole() != null) {
            userToUpdate.setUserRole(userUpdateRequest.getUserRole());
        }
        Date currentTime = new Date();
        userToUpdate.setUpdateTime(currentTime);

        // 执行更新操作
        boolean updateResult = this.updateById(userToUpdate);
        if (!updateResult) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "系统内部异常，更新操作失败");
        }

        return updateId;
    }

    @Override
    public List<Userinfo> queryUserRequest(UserQueryRequest userQueryRequest, HttpServletRequest request) {
        // 1. 校验
        if (userQueryRequest.getUserAccount() != null && userQueryRequest.getUserAccount().length() < 4){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号过短");
        }
        QueryWrapper<Userinfo> queryWrapper = new QueryWrapper<>();

        // 添加用户名查询条件
        queryWrapper.like(StringUtils.isNotBlank(userQueryRequest.getUserName()), "userName", userQueryRequest.getUserName());
        // 添加用户账号查询条件
        queryWrapper.like(StringUtils.isNotBlank(userQueryRequest.getUserAccount()), "userAccount", userQueryRequest.getUserAccount());
        // 添加性别查询条件
        queryWrapper.eq(userQueryRequest.getGender() != null, "gender", userQueryRequest.getGender());
        // 添加电话查询条件
        queryWrapper.like(StringUtils.isNotBlank(userQueryRequest.getPhone()), "phone", userQueryRequest.getPhone());
        // 添加邮箱查询条件
        queryWrapper.like(StringUtils.isNotBlank(userQueryRequest.getEmail()), "email", userQueryRequest.getEmail());
        // 添加用户状态查询条件
        queryWrapper.eq(userQueryRequest.getUserStatus() != null, "userStatus", userQueryRequest.getUserStatus());
        // 添加创建时间查询条件
        queryWrapper.ge(userQueryRequest.getCreateTime() != null, "createTime", userQueryRequest.getCreateTime());
        // 添加用户角色查询条件
        queryWrapper.eq(userQueryRequest.getUserRole() != null, "userRole", userQueryRequest.getUserRole());
        List<Userinfo> user = userinfoService.list(queryWrapper);

        for (Userinfo u: user){
            userinfoService.getSafeUserinfo(u);
        }
        return user;
    }

    @Override
    public IPage<Userinfo> queryUserRequest(UserQueryRequest userQueryRequest, HttpServletRequest request, Integer pageNum, Integer pageSize) {
        // 1. 校验
        if (userQueryRequest.getUserAccount() != null && userQueryRequest.getUserAccount().length() < 4){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号过短");
        }

        // 2. 构建查询条件
        QueryWrapper<Userinfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("userName", userQueryRequest.getUserName())
                .like("userAccount",userQueryRequest.getUserAccount())
                .eq("gender", userQueryRequest.getGender())
                .like("phone", userQueryRequest.getPhone())
                .like("email", userQueryRequest.getEmail())
                .eq("userStatus", userQueryRequest.getUserStatus())
                .eq("userRole", userQueryRequest.getUserRole());

        // 3. 执行分页查询
        Page<Userinfo> page = new Page<>(pageNum, pageSize);
        IPage<Userinfo> userPage = userinfoService.page(page, queryWrapper);

        // 4. 脱敏用户信息
        List<Userinfo> userList = userPage.getRecords();
        for (Userinfo userinfo : userList) {
            userinfoService.getSafeUserinfo(userinfo);
        }

        return userPage;
    }

}
