package com.yupi.my_usercenter_backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yupi.my_usercenter_backend.common.BaseResponse;
import com.yupi.my_usercenter_backend.common.ErrorCode;
import com.yupi.my_usercenter_backend.common.ResultUtils;
import com.yupi.my_usercenter_backend.exception.BusinessException;
import com.yupi.my_usercenter_backend.model.Request.UserLoginRequest;
import com.yupi.my_usercenter_backend.model.Request.UserRegisterRequest;
import com.yupi.my_usercenter_backend.model.Userinfo;
import com.yupi.my_usercenter_backend.model.dto.UserAddRequest;
import com.yupi.my_usercenter_backend.model.dto.UserQueryRequest;
import com.yupi.my_usercenter_backend.model.dto.UserUpdateRequest;
import com.yupi.my_usercenter_backend.service.UserManageService;
import com.yupi.my_usercenter_backend.service.UserinfoService;
import com.yupi.my_usercenter_backend.service.impl.UserManageServiceImpl;
import com.yupi.my_usercenter_backend.utils.ExcelUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

import static com.yupi.my_usercenter_backend.constant.UserConstant.ADMIN_ROLE;
import static com.yupi.my_usercenter_backend.constant.UserConstant.USER_LOGIN_STATE;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserinfoService userinfoService;

    @Resource
    private UserManageService userManageService;

    /**
     * 用户注册
     * @param userRegisterRequest
     * @return
     */
    @PostMapping("/register")
    public BaseResponse<Long> UserRegister(@RequestBody UserRegisterRequest userRegisterRequest){
        if (userRegisterRequest == null){
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)){
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        long result = userinfoService.UserRegister(userAccount, userPassword, checkPassword);
        return ResultUtils.success(result);
    }

    /**
     * 用户登录
     * @param userLoginRequest
     * @param request
     * @return
     */
    @PostMapping("/login")
    public BaseResponse<Userinfo> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request){
        if (userLoginRequest == null){
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)){
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        Userinfo userinfo = userinfoService.UserLogin(userAccount, userPassword, request);
        return ResultUtils.success(userinfo);
    }

    /**
     * 获取当前登录用户
     * @param request
     * @return
     */
    @GetMapping("/current")
    public BaseResponse<Userinfo> getCurrentUser(HttpServletRequest request){
        Userinfo userinfo = (Userinfo) request.getSession().getAttribute(USER_LOGIN_STATE);
        if (userinfo == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long userId = userinfo.getId();
        Userinfo currentUser = userinfoService.getById(userId);
        // 这个后台是提供给管理员，所以需要判断权限
        if (currentUser.getIsDelete() == 1 || !isAdmin(request)){
            throw new BusinessException(ErrorCode.AUTH_ERROR);
        }
        Userinfo safeUserinfo = userinfoService.getSafeUserinfo(currentUser);
        return ResultUtils.success(safeUserinfo);
    }

    /**
     * 管理员查询用户（根据姓名查询 或 展示所以用户）
     * @param userName
     * @param request
     * @return
     */
    @GetMapping("/search")
    public BaseResponse<List<Userinfo>> searchUserList(String userName, HttpServletRequest request){
        if (!isAdmin(request)){
            throw new BusinessException(ErrorCode.AUTH_ERROR);
        }
        QueryWrapper<Userinfo> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isAnyBlank(userName)){
            queryWrapper.like("userName", userName);
        }
        List<Userinfo> userinfoList = userinfoService.list(queryWrapper);
        // 脱敏
        List<Userinfo> collect = userinfoList.stream().map(userinfo -> userinfoService.getSafeUserinfo(userinfo)).collect(Collectors.toList());
        return ResultUtils.success(collect);
    }

    /**
     * 删除id用户
     * @param id
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUser(@RequestBody long id, HttpServletRequest request){
        if (!isAdmin(request)){
            throw new BusinessException(ErrorCode.AUTH_ERROR, "缺少管理员权限");
        }
        if (id < 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数id错误");
        }
        boolean b = userinfoService.removeById(id);
        return ResultUtils.success(b);
    }


    /**
     * 判断是否管理员
     * @param request
     * @return
     */
    public boolean isAdmin(HttpServletRequest request){
        Userinfo userinfo = (Userinfo) request.getSession().getAttribute(USER_LOGIN_STATE);
        return userinfo != null && userinfo.getUserStatus() == ADMIN_ROLE;
    }

    /**
     * 批量插入
     * @param file
     * @return
     */
    @PostMapping("/importUsers")
    public ResponseEntity<String> importUsers(@RequestPart("file") MultipartFile file) {
        try {
            List<Userinfo> users = ExcelUtils.parseExcel(file.getInputStream());
            userManageService.addUserListRequest(users);
            return ResponseEntity.ok("Users imported successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to import users.");
        }
    }

    @PostMapping("/add")
    public BaseResponse<Long> addUser(@RequestBody UserAddRequest userAddRequest, HttpServletRequest request){
        if (!isAdmin(request)){
            throw new BusinessException(ErrorCode.AUTH_ERROR);
        }
        Long userId = userManageService.addUserRequest(userAddRequest,request);
        return ResultUtils.success(userId);
    }
    @PostMapping("/update")
    public BaseResponse<Long> delUser(@RequestBody UserUpdateRequest userUpdateRequest, HttpServletRequest request){
        if (!isAdmin(request)){
            throw new BusinessException(ErrorCode.AUTH_ERROR);
        }
        Long userId = userManageService.updateUserRequest(userUpdateRequest,request);
        return ResultUtils.success(userId);
    }
    @PostMapping("/query")
    public BaseResponse<List<Userinfo>> queryUser(@RequestBody UserQueryRequest userQueryRequest, HttpServletRequest request){
//        if (!isAdmin(request)){
//            throw new BusinessException(ErrorCode.AUTH_ERROR);
//        }
        List<Userinfo> userinfo = userManageService.queryUserRequest(userQueryRequest, request);
        return ResultUtils.success(userinfo);
    }

}
