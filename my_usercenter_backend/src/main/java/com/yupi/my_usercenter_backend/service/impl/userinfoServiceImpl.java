package com.yupi.my_usercenter_backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yupi.my_usercenter_backend.mapper.userinfoMapper;
import com.yupi.my_usercenter_backend.model.userinfo;
import com.yupi.my_usercenter_backend.service.userinfoService;
import org.springframework.stereotype.Service;

/**
* @author poise
* @description 针对表【userinfo(用户信息表)】的数据库操作Service实现
* @createDate 2024-04-07 10:47:17
*/
@Service
public class userinfoServiceImpl extends ServiceImpl<userinfoMapper, userinfo>
    implements userinfoService {

}




