package com.client.bridge.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.client.bridge.model.dto.UserDTO;
import com.client.bridge.model.utils.DateTimeConstans;
import com.client.bridge.model.utils.URLConstans;
import com.client.bridge.model.wrapper.UserResultDTO;

@Service
public class BridgeServiceImpl implements BridgeService {

	@Autowired
	private RestTemplate template;

	@Autowired
	private UserBridgeService userService;

	@Autowired
	private StockBridgeService stockService;
	
	@Autowired
	private Environment env;

	@Override
	public UserBridgeService getUserService() {

		UserResultDTO userLoggedResponse = getUserResultDTO();

		userService.setUserLoggValidate(userLoggedResponse);

		return userService;
	}

	@Override
	public StockBridgeService getStockService() {

		UserResultDTO userLoggedResponse = getUserResultDTO();

		stockService.setUserLoggValidate(userLoggedResponse);

		return stockService;
	}

	private UserResultDTO getUserLoggedResponse(UserDTO userLogged) {
		UserResultDTO userLoggedResponse = null;

		//Validating userLogged
		if (UserDTO.checkLoggedUser(userLogged)) {

			long minutesDiff = userLogged.getLastTimeLogged() != null
					? Duration.between(userLogged.getLastTimeLogged(), LocalDateTime.now()).toMinutes()
					: 1;
			
			//Validating userLogged
			if (minutesDiff < DateTimeConstans.MAX_MINUTES_UNLOG)
			{
				List<UserDTO> userLogList = new ArrayList<>();
				userLogList.add(userLogged);
				
				userLoggedResponse = new UserResultDTO(userLogList);
			}
			else
			{
				template.put(URLConstans.USERS_URL + "/users/logged/" + userLogged.getId() + "/" + false + "/"
						+ LocalDateTime.now(), null);

				userLoggedResponse = new UserResultDTO(env.getProperty("error_unlogged_code"),
						env.getProperty("error_unlogged_description"));
			}

		} else {
			userLoggedResponse = new UserResultDTO(env.getProperty("error_message_not_user_logged_code"),
					env.getProperty("error_message_not_user_logged_description"));
		}
		return userLoggedResponse;
	}
	
	private UserResultDTO getUserResultDTO() {
		
		UserDTO userLogged = template.getForObject(URLConstans.USERS_URL + "/users/logged", UserDTO.class);

		UserResultDTO userLoggedResponse = getUserLoggedResponse(userLogged);
		return userLoggedResponse;
	}

}
