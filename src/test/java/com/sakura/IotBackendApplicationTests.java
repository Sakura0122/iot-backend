package com.sakura;

import com.sakura.mapper.system.SysUserMapper;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class IotBackendApplicationTests {

	@Resource
	private SysUserMapper sysUserMapper;

	@Test
	void contextLoads() {

	}

}
