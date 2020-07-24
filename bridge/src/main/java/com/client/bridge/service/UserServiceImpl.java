package com.client.bridge.service;

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
			result = new UserResultDTO(this.userLoggedInfo.getResponseCode(),
					this.userLoggedInfo.getResponseDescription());
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

		BaseResultDTO response = new BaseResultDTO(this.userLoggedInfo.getResponseCode(),
				this.userLoggedInfo.getResponseDescription());
		return response;
	}

	@Override
	public BaseResultDTO createUserService(UserDTO user) {

		BaseResultDTO response = new BaseResultDTO();

		UserDTO userLogged = userLoggedInfo.getUserArray() != null
				? userLoggedInfo.getUserArray().stream().findFirst().get()
				: null;

		if (UserDTO.checkLoggedUser(userLogged)) {
			UserDTO userFinded = template.getForObject(URLConstans.USERS_URL + "/users/search/" + user.getUserName(),
					UserDTO.class);

			if (userFinded == null) {
				user.setCreatedDateAndCreatedBy(LocalDate.now(), userLogged.getFullName());
				
				if (userLogged.getRole().equalsIgnoreCase(Roles.ADMIN_ROLE)) {
					
					template.postForEntity(URLConstans.USERS_URL + "/users", user, String.class);
					return response;
					
				} else {
					if (user.getRole().equalsIgnoreCase(Roles.ADMIN_ROLE)) {
						
						response.setResponseCode("535");
						response.setResponseDescription("Your role doesn´t allow you to asigned ADMIN Role to a new User");
						return response;
						
					} else {
						
						template.postForLocation(URLConstans.USERS_URL + "/users", user);
						return response;
						
					}
				}
			} else {
				response.setResponseCode("541");
				response.setResponseDescription("This UserName already exists in the system");
			}
			
			
		} else {
			response = new BaseResultDTO(this.userLoggedInfo.getResponseCode(),
					this.userLoggedInfo.getResponseDescription());
		}

		return response;
	}

	@Override
	public BaseResultDTO deleteUserService(int id) {

		BaseResultDTO response = new BaseResultDTO();

		UserDTO userLogged = userLoggedInfo.getUserArray() != null
				? userLoggedInfo.getUserArray().stream().findFirst().get()
				: null;

		if (UserDTO.checkLoggedUser(userLogged)) {
			if (userLogged.getRole().equalsIgnoreCase(Roles.ADMIN_ROLE)) {
				
				UserDTO userFinded = template.getForObject(URLConstans.USERS_URL + "/users/" + id,
						UserDTO.class);
				if(userFinded != null) 
				{
					template.delete(URLConstans.USERS_URL + "/users/"+userFinded.getId());
				}
				else
				{
					response.setResponseCode("555");
					response.setResponseDescription("The user provided is not registed in the system");
				}
			}
			else
			{
				response.setResponseCode("550");
				response.setResponseDescription("You are not allow to delete an user, please contact an Admin.");
			}
		}
		else
		{
			response = new BaseResultDTO(this.userLoggedInfo.getResponseCode(),
					this.userLoggedInfo.getResponseDescription());
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

				if (user.getPassword().equals(password)) {

					template.put(URLConstans.USERS_URL + "/users/logged/" + user.getId() + "/" + true + "/"
							+ LocalDateTime.now(), null);
					List<UserDTO> listTemp = new ArrayList<UserDTO>();
					user.setLogged(true);
					listTemp.add(user);
					result.setUserArray(listTemp);

				} else {

					result.setResponseCode("532");
					result.setResponseDescription("The password is incorrect");

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
	public void setUserLoggValidate(UserResultDTO userLoggedResult) {

		if (userLoggedResult != null) {
			this.userLoggedInfo = userLoggedResult;
		}
	}

}
