package com.client.bridge.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
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
	private RestTemplate template;

	private UserResultDTO userLoggedInfo;
	
	@Autowired
	private Environment env;

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
						
						response.setResponseCode(env.getProperty("error_message_user_not_allow_code"));
						response.setResponseDescription(env.getProperty("error_message_user_not_allow_description"));
						return response;
						
					} else {
						
						template.postForLocation(URLConstans.USERS_URL + "/users", user);
						return response;
						
					}
				}
			} else {
				response.setResponseCode(env.getProperty("error_message_username_already_exists_code"));
				response.setResponseDescription(env.getProperty("error_message_username_already_exists_description"));
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
					response.setResponseCode(env.getProperty("error_message_user_not_registered_code"));
					response.setResponseDescription(env.getProperty("error_message_user_not_registered_description"));
				}
			}
			else
			{
				response.setResponseCode(env.getProperty("error_message_user_not_allow_code"));
				response.setResponseDescription(env.getProperty("error_message_user_not_allow_description"));
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

					result.setResponseCode(env.getProperty("error_message_user_password_incorrect_code"));
					result.setResponseDescription(env.getProperty("error_message_user_password_incorrect_description"));

				}

			} else {
				result.setResponseCode(env.getProperty("error_message_user_not_registered_code"));
				result.setResponseDescription(env.getProperty("error_message_user_not_registered_description"));
			}

		} else {
			result.setResponseCode(env.getProperty("error_message_user_already_log_in_code"));
			result.setResponseDescription(env.getProperty("error_message_user_already_log_in_description"));
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
