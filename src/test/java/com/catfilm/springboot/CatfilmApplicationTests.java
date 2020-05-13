package com.catfilm.springboot;

import com.catfilm.springboot.controller.user.vo.UserInfo;
import com.catfilm.springboot.service.user.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CatfilmApplicationTests {

	@Test
	void contextLoads() {
	}

	@Autowired
	private UserService userService;



}
