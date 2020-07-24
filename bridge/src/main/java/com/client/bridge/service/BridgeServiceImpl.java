package com.client.bridge.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.client.bridge.model.dto.UserDTO;
import com.client.bridge.model.utils.DateTimeConstans;
import com.client.bridge.model.utils.URLConstans;
import com.client.bridge.model.wrapper.UserResultDTO;

@Service
public class BridgeServiceImpl implements BridgeService {

	@Autowired
	RestTemplate template;

	@Autowired
	UserBridgeService userService;

	@Autowired
	StockBridgeService stockService;

	@Override
	public UserBridgeService getUserService() {

		UserDTO userLogged = template.getForObject(URLConstans.USERS_URL + "/users/logged", UserDTO.class);

		UserResultDTO userLoggedResponse = getUserLoggedResponse(userLogged);

		userService.setUserLoggValidate(userLoggedResponse);

		return userService;
	}

	@Override
	public StockBridgeService getStockService() {

		UserDTO userLogged = template.getForObject(URLConstans.USERS_URL + "/users/logged", UserDTO.class);

		UserResultDTO userLoggedResponse = getUserLoggedResponse(userLogged);

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

				userLoggedResponse = new UserResultDTO("522",
						"Time of Logging has been exceded, you are now LogOut");
			}

		} else {
			userLoggedResponse = new UserResultDTO("520",
					"There is not user logged into the application, please LogIn first");
		}
		return userLoggedResponse;
	}

}
