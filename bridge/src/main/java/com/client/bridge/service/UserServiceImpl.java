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

import com.client.bridge.model.dto.UserDTO;
import com.client.bridge.model.generic.BaseResultDTO;
import com.client.bridge.model.role.Roles;
import com.client.bridge.model.utils.DateTimeConstans;
import com.client.bridge.model.utils.URLConstans;
import com.client.bridge.model.wrapper.UserResultDTO;

@Service
public class UserServiceImpl implements UserBridgeService {
	@Autowired
	RestTemplate template;

	private UserResultDTO userLoggedInfo;

	@Override
	public UserResultDTO retrieveAllUsers() {

		UserResultDTO result = new UserResultDTO();

		UserDTO userLogged = userLoggedInfo.getUserArray() != null
				? userLoggedInfo.getUserArray().stream().findFirst().get()
				: null;

		if (UserDTO.checkLoggedUser(userLogged)) {
			UserDTO[] usersRegistered = template.getForObject(URLConstans.USERS_URL + "/users", UserDTO[].class);
			if (userLogged.getRole().equalsIgnoreCase(Roles.ADMIN_ROLE)) {
				result.setUserArray(Arrays.asList(usersRegistered));
			} else {
				result.setUserArray(Arrays.asList(usersRegistered).stream()
						.filter(i -> i.getRole().equals(Roles.USER_ROLE)).collect(Collectors.toList()));
			}
		} else {
			result = new UserResultDTO(this.userLoggedInfo.getResponseCode(),this.userLoggedInfo.getResponseDescription());
		}

		return result;
	}

	@Override
	public BaseResultDTO logOutService() {

		UserDTO userLogged = userLoggedInfo.getUserArray() != null
				? userLoggedInfo.getUserArray().stream().findFirst().get()
				: null;

		if (UserDTO.checkLoggedUser(userLogged)) {
			template.put(URLConstans.USERS_URL + "/users/logged/" + userLogged.getId() + "/" + false + "/"
					+ LocalDateTime.now(), null);
			return new BaseResultDTO();
		}

		BaseResultDTO response = new BaseResultDTO(this.userLoggedInfo.getResponseCode(),this.userLoggedInfo.getResponseDescription());
		return response;
	}

	@Override
	public BaseResultDTO createUserService(UserDTO user) {

		BaseResultDTO response = new BaseResultDTO();

		UserDTO userLogged = userLoggedInfo.getUserArray() != null
				? userLoggedInfo.getUserArray().stream().findFirst().get()
				: null;

		if (UserDTO.checkLoggedUser(userLogged)) {
			user.setCreatedDateAndCreatedBy(LocalDate.now(), userLogged.getFullName());
			if (userLogged.getRole().equalsIgnoreCase(Roles.ADMIN_ROLE)) {
				template.postForEntity(URLConstans.USERS_URL + "/users", user, String.class);
				return response;
			} else {
				if (user.getRole().equalsIgnoreCase(Roles.ADMIN_ROLE)) {
					response.setResponseCode("535");
					response.setResponseDescription("Your role dont allow you to asigned ADMIN Role to a new User");
					return response;
				} else {
					template.postForLocation(URLConstans.USERS_URL + "/users", user);
					return response;
				}
			}
		} else {
			response = new BaseResultDTO(this.userLoggedInfo.getResponseCode(),this.userLoggedInfo.getResponseDescription());
		}

		return response;
	}

	@Override
	public UserResultDTO loggingService(String userName, String password) {

		UserResultDTO result = new UserResultDTO();
		
		UserDTO userLogged = userLoggedInfo.getUserArray() != null
				? userLoggedInfo.getUserArray().stream().findFirst().get()
				: null;

		if (userLogged == null) {
			UserDTO user = template.getForObject(URLConstans.USERS_URL + "/users/search/" + userName, UserDTO.class);

			if (user != null) {

				long minutesDiff = user.getLastTimeLogged() != null
						? Duration.between(user.getLastTimeLogged(), LocalDateTime.now()).toMinutes()
						: 1;

				if (minutesDiff < DateTimeConstans.MAX_MINUTES_UNLOG
						|| user.getRole().equalsIgnoreCase(Roles.ADMIN_ROLE)) {

					if (user.getPassword().equals(password)) {

						if (user.isActive()) {

							template.put(URLConstans.USERS_URL + "/users/logged/" + user.getId() + "/" + true + "/"
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
				} else {
					template.put(URLConstans.USERS_URL + "/users/status/" + user.getId() + "/" + false, null);

					result.setResponseCode("531");
					result.setResponseDescription(
							"The User trying to Log In hasn´t logg in a long time, his status is now inactive");
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
	public BaseResultDTO changeUserStatusService(String userName, boolean active) {

		BaseResultDTO response = new BaseResultDTO();

		UserDTO userLogged = userLoggedInfo.getUserArray() != null
				? userLoggedInfo.getUserArray().stream().findFirst().get()
				: null;

		if (UserDTO.checkLoggedUser(userLogged)) {
			if (userLogged.getRole().equalsIgnoreCase(Roles.ADMIN_ROLE)) {
				UserDTO user = template.getForObject(URLConstans.USERS_URL + "/users/search/" + userName,
						UserDTO.class);
				if (user != null) {
					template.put(URLConstans.USERS_URL + "/users/status/" + user.getId() + "/" + active, null);
				} else {
					response.setResponseCode("531");
					response.setResponseDescription("The userName provided is not registered");
				}

			} else {
				response.setResponseCode("547");
				response.setResponseDescription("You are not allow to change the status of this user");
			}
		} else {
			response = new BaseResultDTO(this.userLoggedInfo.getResponseCode(),this.userLoggedInfo.getResponseDescription());
		}

		return response;
	}

	@Override
	public void setUserLoggValidate(UserResultDTO userLoggedResult) {

		if (userLoggedResult != null) {
			this.userLoggedInfo = userLoggedResult;
		}
	}

}
