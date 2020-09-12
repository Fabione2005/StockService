package com.client.bridge.service.hystrix;

import com.client.bridge.model.dto.UserDTO;

public interface LogOutUserService {

	void logOutUser(UserDTO userLogged);
	
}
