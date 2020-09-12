package com.client.bridge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.web.client.RestTemplate;

@ComponentScan(basePackages = {"com.client.bridge.controller","com.client.bridge.service","com.client.bridge.app"})
@SpringBootApplication
@EnableCircuitBreaker
@EnableHystrixDashboard
public class ClientServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClientServiceApplication.class, args);
	}
	
	@Bean
	@LoadBalanced
	public RestTemplate template() {
		RestTemplate template=new RestTemplate();
		BasicAuthenticationInterceptor intercep;	
		intercep=new BasicAuthenticationInterceptor("admin", "admin");	
		template.getInterceptors().add(intercep);
		return template;

	}
}
