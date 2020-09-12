package com.client.bridge.service.hystrix;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.client.bridge.exception.UserLoggingException;
import com.client.bridge.model.dto.UserDTO;
import com.client.bridge.model.utils.URLConstans;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

@Service
public class RetrieveUserLoggedServiceImpl implements RetrieveUserLoggedService {

	@Autowired
	private RestTemplate template;

	@HystrixCommand(fallbackMethod = "getFallbackUserLogged", commandProperties = {
			@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "2000"),
			@HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "5"),
			@HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "50"),
			@HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "5000"), })
	@Override
	public UserDTO getUserLogged() {

		UserDTO userLogged = template.getForObject(URLConstans.USERS_URL + "/users/logged", UserDTO.class);

		return userLogged;
	}

	public UserDTO getFallbackUserLogged() {

		throw new UserLoggingException("There was an error, please try again later",HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
