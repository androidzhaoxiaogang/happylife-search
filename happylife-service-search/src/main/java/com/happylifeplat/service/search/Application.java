package com.happylifeplat.service.search;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import com.happylifeplat.service.search.annotation.IgnoreScanInTest;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableAsync;

@IgnoreScanInTest
@SpringBootApplication
@EnableAsync
@EnableApolloConfig
@ImportResource({"classpath:spring/applicationContext.xml"})
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
