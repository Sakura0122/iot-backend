package com.sakura;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

@SpringBootTest
class IotBackendApplicationTests {

	@Autowired
	private StringRedisTemplate redisTemplate;

	@Test
	void contextLoads() {
		redisTemplate.opsForHash().put("role_perms:123","user:button:","user:button:add");
		redisTemplate.opsForHash().put("role_perms:123","user:menu:","sysUser");
		redisTemplate.opsForHash().put("role_perms:123","user:role:","admin");
	}

}
