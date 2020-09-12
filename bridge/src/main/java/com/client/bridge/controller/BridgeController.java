package com.client.bridge.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.client.bridge.model.dto.StockDTO;
import com.client.bridge.model.dto.UserDTO;
import com.client.bridge.model.generic.BaseResultDTO;
import com.client.bridge.model.wrapper.StockResultDTO;
import com.client.bridge.model.wrapper.UserResultDTO;
import com.client.bridge.service.BridgeService;

@RestController
public class BridgeController {

	@Autowired
	BridgeService service;

	@GetMapping(value = "/stocks", produces = MediaType.APPLICATION_JSON_VALUE)
	public StockResultDTO retrieveAllStocks() 
	{
		return service.getStockService().retrieveAllStocks();
	}
	
	@GetMapping(value = "/users/all", produces = MediaType.APPLICATION_JSON_VALUE)
	public UserResultDTO retrieveAllUsers()
	{
		return service.getUserService(false).retrieveAllUsers();
	}
	
	@PutMapping(value = "/users/logout", produces = MediaType.APPLICATION_JSON_VALUE)
	public BaseResultDTO logOutService() 
	{
		return service.getUserService(false).logOutService();
	}
	
	@PostMapping(value = "/users/addUser",consumes=MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public BaseResultDTO createUserService(@RequestBody UserDTO user) 
	{
		return service.getUserService(true).createUserService(user);
	}
	
	@PostMapping(value = "/stocks/addStock",consumes=MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public BaseResultDTO createStockService(@RequestBody StockDTO stock) 
	{
		return service.getStockService().createStockService(stock);
	}
	
	@GetMapping(value = "/users/login/{userName}/{password}", produces = MediaType.APPLICATION_JSON_VALUE)
	public UserResultDTO loggingService(@PathVariable String userName,@PathVariable String password) 
	{
		return service.getUserService(true).loggingService(userName, password);
	}
	
	@DeleteMapping(value="stocks/{id}")
	public BaseResultDTO deleteStock(@PathVariable int id) {
		return service.getStockService().deleteStockService(id);
	}
	
	@DeleteMapping(value="user/{id}")
	public BaseResultDTO deleteUser(@PathVariable int id) {
		return service.getUserService(false).deleteUserService(id);
	}
}
