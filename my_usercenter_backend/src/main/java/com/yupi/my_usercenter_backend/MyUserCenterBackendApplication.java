package com.yupi.my_usercenter_backend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.yupi.my_usercenter_backend.mapper")
public class MyUserCenterBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyUserCenterBackendApplication.class, args);
	}

}
