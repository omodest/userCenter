package com.yupi.my_usercenter_backend;

import com.yupi.my_usercenter_backend.mapper.UserinfoMapper;

import com.yupi.my_usercenter_backend.model.Userinfo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Date;

@SpringBootTest
class UserTest {

    @Resource
    public UserinfoMapper userinfoMapper;
    @Test
    void testAdd(){
        Userinfo userInfo = new Userinfo();
        // 设置字段值
//        userInfo.setId(1L);
        userInfo.setUserName("John Doe");
        userInfo.setUserAccount("john_doe123");
        userInfo.setUserAvatarUrl("http://example.com/avatar.jpg");
        userInfo.setGender(1); // 假设1代表男性
        userInfo.setUserPassword("password123");
        userInfo.setPhone("1234567890");
        userInfo.setEmail("john@example.com");
        userInfo.setUserStatus("0");
        userInfo.setCreateTime(new Date());
        userInfo.setUpdateTime(new Date());
        userInfo.setIsDelete(0);
        userInfo.setUserRole(1); // 假设1代表普通用户

        int result = userinfoMapper.insert(userInfo);
        Assertions.assertEquals(1, result);

    }

}
