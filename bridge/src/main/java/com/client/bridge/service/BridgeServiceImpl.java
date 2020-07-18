package com.client.bridge.service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.client.bridge.model.dto.StockDTO;
import com.client.bridge.model.dto.UserDTO;
import com.client.bridge.model.generic.BaseResultDTO;
import com.client.bridge.model.role.Roles;
import com.client.bridge.model.utils.DateTimeConstans;
import com.client.bridge.model.wrapper.StockResultDTO;
import com.client.bridge.model.wrapper.UserResultDTO;

@Service
public class BridgeServiceImpl implements BridgeService {

	String urlBaseStocks = "http://stocks-service";
	String urlBaseUsers = "http://users-service";

	@Autowired
	RestTemplate template;

	@Override
	public StockResultDTO retrieveAllStocks() {

		StockResultDTO stockWrapper = new StockResultDTO();

		UserDTO userLogged = template.getForObject(urlBaseUsers + "/users/logged", UserDTO.class);

		if (checkLoggedUser(userLogged)) {
			StockDTO[] stocks = template.getForObject(urlBaseStocks + "/stocks", StockDTO[].class);

			String message = null;
			String code = null;

			message = stocks.length == 0 ? "no stocks where found" : "";
			code = stocks.length == 0 ? "4154" : "0";

			stockWrapper.setStockArray(Arrays.asList(stocks));
			stockWrapper.setResponseCode(code);
			stockWrapper.setResponseDescription(message);
		} else {
			stockWrapper.setResponseCode("533");
			stockWrapper.setResponseDescription("You are not Logged, please first log into the application");
		}

		return stockWrapper;
	}

	@Override
	public UserResultDTO retrieveAllUsers() {

		UserResultDTO result = new UserResultDTO();

		UserDTO userLogged = template.getForObject(urlBaseUsers + "/users/logged", UserDTO.class);

		if (checkLoggedUser(userLogged)) {
			UserDTO[] usersRegistered = template.getForObject(urlBaseUsers + "/users", UserDTO[].class);
			if (userLogged.getRole().equalsIgnoreCase(Roles.ADMIN_ROLE)) {
				result.setUserArray(Arrays.asList(usersRegistered));
			} else {
				result.setUserArray(Arrays.asList(usersRegistered).stream()
						.filter(i -> i.getRole().equals(Roles.USER_ROLE)).collect(Collectors.toList()));
			}
		} else {
			result.setResponseCode("533");
			result.setResponseDescription("You are not Logged, please first log into the application");
		}

		return result;
	}

	@Override
	public BaseResultDTO logOutService() {

		UserDTO userLogged = template.getForObject(urlBaseUsers + "/users/logged", UserDTO.class);

		if (checkLoggedUser(userLogged)) {
			template.put(urlBaseUsers + "/users/logged/" + userLogged.getId() + "/" + false + "/" + LocalDateTime.now(),
					null);
			return new BaseResultDTO();
		}

		BaseResultDTO response = new BaseResultDTO();
		response.setResponseCode("534");
		response.setResponseDescription("There is not user Logged in to the application");
		return response;
	}

	@Override
	public BaseResultDTO createUserService(UserDTO user) {

		BaseResultDTO response = new BaseResultDTO();

		UserDTO userLogged = template.getForObject(urlBaseUsers + "/users/logged", UserDTO.class);

		if (checkLoggedUser(userLogged)) {
			user.setCreatedDateAndCreatedBy(LocalDate.now(), userLogged.getFullName());
			if (userLogged.getRole().equalsIgnoreCase(Roles.ADMIN_ROLE)) {
				template.postForEntity(urlBaseUsers + "/users", user, String.class);
				return response;
			} else {
				if (user.getRole().equalsIgnoreCase(Roles.ADMIN_ROLE)) {
					response.setResponseCode("535");
					response.setResponseDescription("Your role dont allow you to asigned ADMIN Role to a new User");
					return response;
				} else {
					template.postForLocation(urlBaseUsers + "/users", user);
					return response;
				}
			}
		} else {
			response.setResponseCode("534");
			response.setResponseDescription("There is not user Logged in to the application");
		}

		return response;
	}

	@Override
	public UserResultDTO loggingService(String userName, String password) {

		UserResultDTO result = new UserResultDTO();
		UserDTO userLogged = template.getForObject(urlBaseUsers + "/users/logged", UserDTO.class);

		if (userLogged == null) {
			UserDTO user = template.getForObject(urlBaseUsers + "/users/search/" + userName, UserDTO.class);

			if (user != null) {
				
				long minutesDiff = user.getLastTimeLogged() != null 
						? Duration.between(user.getLastTimeLogged(), LocalDateTime.now()).toMinutes()
								: 1;
				
				if (minutesDiff < DateTimeConstans.MAX_MINUTES_UNLOG || user.getRole().equalsIgnoreCase(Roles.ADMIN_ROLE)) {
					
					if (user.getPassword().equals(password)) {
						
						if (user.isActive()) {
							
							template.put(urlBaseUsers + "/users/logged/" + user.getId() + "/" + true + "/"
									+ LocalDateTime.now(), null);
							List<UserDTO> listTemp = new ArrayList<UserDTO>();
							user.setLogged(true);
							listTemp.add(user);
							result.setUserArray(listTemp);
						} else {
							
							result.setResponseCode("585");
							result.setResponseDescription(
									"The user is not active, if you want to activate the user, ask for a admin user");
						}
						
					} else {
						
						result.setResponseCode("532");
						result.setResponseDescription("The password is incorrect");
						
					}
				}
				else
				{
					template.put(urlBaseUsers + "/users/status/" + user.getId() + "/" + false,null);
					
					result.setResponseCode("531");
					result.setResponseDescription("The User trying to Log In hasn´t logg in a long time, his status is now inactive");
				}
			} else {
				result.setResponseCode("531");
				result.setResponseDescription("The userName provided is not registered");
			}

		} else {
			result.setResponseCode("533");
			result.setResponseDescription(
					"The user " + userLogged.getUserName() + " is logged, if you want to logIn please logOut first");
		}

		return result;
	}

	@Override
	public BaseResultDTO createStockService(StockDTO stock) {

		BaseResultDTO response = new BaseResultDTO();

		UserDTO userLogged = template.getForObject(urlBaseUsers + "/users/logged", UserDTO.class);

		if (checkLoggedUser(userLogged)) {
			if (userLogged.getRole().equalsIgnoreCase(Roles.ADMIN_ROLE)) {
				if (stock != null) {
					LocalDate localDate = LocalDate.now();
					stock.setLastUpdate(String.valueOf(localDate));
					template.postForEntity(urlBaseStocks + "/stocks", stock, String.class);
					return response;
				}
			} else {
				response.setResponseCode("540");
				response.setResponseDescription("You are not allow to create a new stock");
			}
		} else {
			response.setResponseCode("534");
			response.setResponseDescription("There is not user Logged in to the application");
		}

		return response;
	}

	private boolean checkLoggedUser(UserDTO userLogged) {
		return userLogged != null && userLogged.isActive();
	}

	@Override
	public BaseResultDTO changeUserStatusService(String userName, boolean active) {

		BaseResultDTO response = new BaseResultDTO();

		UserDTO userLogged = template.getForObject(urlBaseUsers + "/users/logged", UserDTO.class);

		if (checkLoggedUser(userLogged)) {
			if (userLogged.getRole().equalsIgnoreCase(Roles.ADMIN_ROLE)) {
				UserDTO user = template.getForObject(urlBaseUsers + "/users/search/" + userName, UserDTO.class);
				if(user != null) 
				{
					template.put(urlBaseUsers + "/users/status/" + user.getId() + "/" + active, null);
				}
				else
				{
					response.setResponseCode("531");
					response.setResponseDescription("The userName provided is not registered");
				}
				
			} else {
				response.setResponseCode("547");
				response.setResponseDescription("You are not allow to change the status of this user");
			}
		} else {
			response.setResponseCode("534");
			response.setResponseDescription("There is not user Logged in to the application");
		}

		return response;
	}

}
