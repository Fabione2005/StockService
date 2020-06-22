package com.client.bridge.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.client.bridge.model.dto.StockDTO;
import com.client.bridge.model.dto.UserDTO;
import com.client.bridge.model.generic.BaseResultDTO;
import com.client.bridge.model.role.Roles;
import com.client.bridge.model.wrapper.StockResultDTO;
import com.client.bridge.model.wrapper.UserResultDTO;

@RestController
public class BridgeController {

	String urlBaseStocks = "http://localhost:8080";
	String urlBaseUsers = "http://localhost:9090";

	@Autowired
	RestTemplate template;

	@GetMapping(value = "/stocks", produces = MediaType.APPLICATION_JSON_VALUE)
	public StockResultDTO retrieveAllStocks() {

		StockResultDTO stockWrapper = new StockResultDTO();
		
		UserDTO userLogged = template.getForObject(urlBaseUsers + "/users/logged", UserDTO.class);
		
		if(userLogged != null) 
		{
			StockDTO[] stocks = template.getForObject(urlBaseStocks + "/stocks", StockDTO[].class);

			String message = null;
			String code = null;

			message = stocks.length == 0 ? "no stocks where found" : "";
			code = stocks.length == 0 ? "4154" : "0";

			stockWrapper.setStockArray(Arrays.asList(stocks));
			stockWrapper.setResponseCode(code);
			stockWrapper.setResponseDescription(message);
		}
		else
		{
			stockWrapper.setResponseCode("533");
			stockWrapper.setResponseDescription("You are not Logged, please first log into the application");
		}
		
		return stockWrapper;
	}
	
	@GetMapping(value = "/users/all", produces = MediaType.APPLICATION_JSON_VALUE)
	public UserResultDTO retrieveAllUsers() 
	{
		UserResultDTO result = new UserResultDTO();
		
		UserDTO userLogged = template.getForObject(urlBaseUsers + "/users/logged", UserDTO.class);
		
		if(userLogged != null) 
		{
			UserDTO[] usersRegistered = template.getForObject(urlBaseUsers + "/users", UserDTO[].class);
			if(userLogged.getRole().equalsIgnoreCase(Roles.ADMIN_ROLE)) 
			{
				result.setUserArray(Arrays.asList(usersRegistered));
			}
			else
			{
				result.setUserArray(Arrays.asList(usersRegistered).stream()
						.filter(i -> i.getRole().equals(Roles.USER_ROLE)).collect(Collectors.toList()));
			}
		}
		else
		{
			result.setResponseCode("533");
			result.setResponseDescription("You are not Logged, please first log into the application");
		}
		
		return result;
	}
	
	@PutMapping(value = "/users/logout", produces = MediaType.APPLICATION_JSON_VALUE)
	public BaseResultDTO logOutService() 
	{
		UserDTO userLogged = template.getForObject(urlBaseUsers + "/users/logged", UserDTO.class);
		
		if(userLogged != null) 
		{
			template.put(urlBaseUsers + "users/logged/"+userLogged.getId()+"/"+false, null);
			return new BaseResultDTO();
		}
		
		BaseResultDTO response = new BaseResultDTO();
		response.setResponseCode("534");
		response.setResponseDescription("There is not user Logged in to the application");
		return response;
	}
	
	@PostMapping(value = "/users/addUser",consumes=MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public BaseResultDTO createUserService(@RequestBody UserDTO user) 
	{
		BaseResultDTO response = new BaseResultDTO(); 
		
		UserDTO userLogged = template.getForObject(urlBaseUsers + "/users/logged", UserDTO.class);
		
		if(userLogged != null) 
		{
			user.setCreatedDateAndCreatedBy(LocalDate.now(), userLogged.getFullName());
			if(userLogged.getRole().equalsIgnoreCase(Roles.ADMIN_ROLE)) 
			{
				template.postForEntity(urlBaseUsers+"/users", user, String.class);
				return response;
			}
			else
			{
				if(user.getRole().equalsIgnoreCase(Roles.ADMIN_ROLE)) 
				{
					response.setResponseCode("535");
					response.setResponseDescription("Your role dont allow you to asigned ADMIN Role to a new User");
					return response;
				}
				else
				{
					template.postForLocation(urlBaseUsers+"/users", user);
					return response;
				}
			}
		}
		
		return response;
	}
	
	@GetMapping(value = "/users/login/{userName}/{password}", produces = MediaType.APPLICATION_JSON_VALUE)
	public UserResultDTO loggingService(@PathVariable String userName,@PathVariable String password) 
	{
		UserResultDTO result = new UserResultDTO();
		UserDTO userLogged = template.getForObject(urlBaseUsers + "/users/logged", UserDTO.class);
		
		if(userLogged == null) 
		{
			UserDTO user = template.getForObject(urlBaseUsers + "/users/search/"+userName, UserDTO.class);
			
			if(user != null) 
			{
				if(user.getPassword().equals(password)) 
				{
					template.put(urlBaseUsers + "users/logged/"+user.getId()+"/"+true, null);
					List<UserDTO> listTemp = new ArrayList<UserDTO>();
					user.setLogged(true);
					listTemp.add(user);
					result.setUserArray(listTemp);
				}
				else
				{
					result.setResponseCode("532");
					result.setResponseDescription("The password is incorrect");
				}
			}
			else
			{
				result.setResponseCode("531");
				result.setResponseDescription("The userName provided is not registered");
			}
		}
		else
		{
			result.setResponseCode("533");
			result.setResponseDescription("The user "+userLogged.getUserName()+" is logged, if you want to logIn please logOut first");
		}
		
		return result;
	}
}
