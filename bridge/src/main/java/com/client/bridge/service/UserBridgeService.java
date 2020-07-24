package com.client.bridge.service;

import com.client.bridge.model.dto.UserDTO;
import com.client.bridge.model.generic.BaseResultDTO;
import com.client.bridge.model.wrapper.UserResultDTO;

public interface UserBridgeService 
{
	void setUserLoggValidate(UserResultDTO userLoggedResult);
	UserResultDTO retrieveAllUsers();
	BaseResultDTO logOutService();
	BaseResultDTO createUserService(UserDTO user);
	BaseResultDTO deleteUserService(int id);
	UserResultDTO loggingService(String userName, String password);
}
