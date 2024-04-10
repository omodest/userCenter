package com.yupi.my_usercenter_backend.model.dto;

import lombok.Data;
import java.util.Date;

@Data
public class UserQueryRequest {

    /**
     * 用户名
     */
    private String userName;

    /**
     * 账号
     */
    private String userAccount;


    /**
     * 性别
     */
    private Integer gender;


    /**
     * 电话
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 用户状态
     */
    private String userStatus;

    /**
     * 创建时间
     */
    private Date createTime;


    /**
     * 用户等级
     */
    private Integer userRole;

    private static final long serialVersionUID = 1L;
}
