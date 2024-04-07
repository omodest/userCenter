package com.yupi.my_usercenter_backend.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yupi.my_usercenter_backend.mapper.UserinfoMapper;
import com.yupi.my_usercenter_backend.model.Userinfo;
import com.yupi.my_usercenter_backend.service.UserinfoService;
import org.springframework.stereotype.Service;


/**
* @author poise
* @description 针对表【userinfo(用户信息表)】的数据库操作Service实现
* @createDate 2024-04-07 17:11:57
*/
@Service
public class UserinfoServiceImpl extends ServiceImpl<UserinfoMapper, Userinfo>
    implements UserinfoService {

}




