package com.client.bridge.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.client.bridge.exception.UserLoggingException;
import com.client.bridge.model.dto.UserDTO;
import com.client.bridge.model.utils.DateTimeConstans;
import com.client.bridge.model.wrapper.UserResultDTO;
import com.client.bridge.service.hystrix.LogOutUserService;
import com.client.bridge.service.hystrix.RetrieveUserLoggedService;

@Service
public class BridgeServiceImpl implements BridgeService {

	
	@Autowired
	private RetrieveUserLoggedService retrieveUser;
	
	@Autowired
	private LogOutUserService logOutUser;
	
	@Autowired
	private UserBridgeService userService;

	@Autowired
	private StockBridgeService stockService;

	@Autowired
	private Environment env;

	@Override
	public UserBridgeService getUserService(boolean isLogging) {

		UserResultDTO userLoggedResponse = getUserResultDTO(isLogging);

		userService.setUserLoggValidate(userLoggedResponse);

		return userService;
	}

	@Override
	public StockBridgeService getStockService() {

		UserResultDTO userLoggedResponse = getUserResultDTO(false);

		stockService.setUserLoggValidate(userLoggedResponse);

		return stockService;
	}

	private UserResultDTO getUserLoggedResponse(UserDTO userLogged, boolean isLogging) {
		UserResultDTO userLoggedResponse = new UserResultDTO();

		// Validating userLogged
		if (!isLogging) {
			
			if(userLogged != null) 
			{
				long minutesDiff = userLogged.getLastTimeLogged() != null
						? Duration.between(userLogged.getLastTimeLogged(), LocalDateTime.now()).toMinutes()
								: 1;
						
						// Validating userLogged
						if (minutesDiff < DateTimeConstans.MAX_MINUTES_UNLOG) {
							List<UserDTO> userLogList = new ArrayList<>();
							userLogList.add(userLogged);
							
							userLoggedResponse = new UserResultDTO(userLogList);
						} else {
							
							logOutUser.logOutUser(userLogged);
							
							throw new UserLoggingException(env.getProperty("error_unlogged_description"), HttpStatus.UNAUTHORIZED);
						}
			}
			else
			{
				throw new UserLoggingException();
			}

		}

		return userLoggedResponse;
	}

	private UserResultDTO getUserResultDTO(boolean isLogging) {

		UserDTO userLogged = retrieveUser.getUserLogged();

		UserResultDTO userLoggedResponse = getUserLoggedResponse(userLogged, isLogging);

		return userLoggedResponse;
	}

}
