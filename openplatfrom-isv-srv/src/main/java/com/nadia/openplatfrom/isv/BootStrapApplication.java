package com.nadia.openplatfrom.isv;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableAsync;


@EnableEurekaClient
@EnableFeignClients("com.nadia")
@SpringBootApplication
@ComponentScan(value = {"com.nadia"})
@ImportResource(locations = { "classpath:mybatis/mapper/**/*.xml" })
@EnableAsync
@MapperScan({"com.nadia.*.dao","com.nadia.*.*.dao"})
public class BootStrapApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(BootStrapApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(BootStrapApplication.class);
	}
}
