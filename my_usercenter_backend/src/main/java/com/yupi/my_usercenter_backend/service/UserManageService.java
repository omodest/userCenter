package com.yupi.my_usercenter_backend.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yupi.my_usercenter_backend.common.BaseResponse;
import com.yupi.my_usercenter_backend.model.Userinfo;
import com.yupi.my_usercenter_backend.model.dto.UserAddRequest;
import com.yupi.my_usercenter_backend.model.dto.UserQueryRequest;
import com.yupi.my_usercenter_backend.model.dto.UserUpdateRequest;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 用户管理接口（管理员操作用户）
 */
public interface UserManageService extends IService<Userinfo> {
    /**
     * 添加用户
     * @param userAddRequest
     * @param request
     * @return
     */
    Long addUserRequest(UserAddRequest userAddRequest, HttpServletRequest request);

    /**
     * 批量添加用户
     * @param userinfoList
     * @return
     */
    boolean addUserListRequest(List<Userinfo> userinfoList);

    /**
     * 修改用户信息
     * @param userUpdateRequest
     * @param request
     * @return
     */
    Long updateUserRequest(UserUpdateRequest userUpdateRequest, HttpServletRequest request);

    /**
     * 用户查询
     * @param userQueryRequest
     * @param request
     * @return
     */
    List<Userinfo> queryUserRequest( UserQueryRequest userQueryRequest, HttpServletRequest request);

    /**
     * 分页查询
     * @param userQueryRequest
     * @param request
     * @param pageNum
     * @param pageSize
     * @return
     */
    IPage<Userinfo> queryUserRequest(UserQueryRequest userQueryRequest, HttpServletRequest request, Integer pageNum, Integer pageSize);



}
