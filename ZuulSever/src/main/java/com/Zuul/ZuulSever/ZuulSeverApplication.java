package com.Zuul.ZuulSever;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@EnableZuulProxy
@SpringBootApplication
public class ZuulSeverApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZuulSeverApplication.class, args);
	}

}
