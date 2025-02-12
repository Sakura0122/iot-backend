package com.sakura;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan({"com.sakura.mapper.system"})
public class IotBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(IotBackendApplication.class, args);
	}

}
