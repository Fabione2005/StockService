package com.client.bridge.model.wrapper;

import java.util.List;

import com.client.bridge.model.dto.UserDTO;
import com.client.bridge.model.generic.BaseResultDTO;

public class UserResultDTO extends BaseResultDTO
{
	
	private List<UserDTO> userArray;
	
	public UserResultDTO(List<UserDTO> userArray) {
		super();
		this.userArray = userArray;
	}
	
	public UserResultDTO(String responseCode, String respondeDescription) 
	{
		super(responseCode,respondeDescription);
	}
	
	public UserResultDTO() 
	{
		super();
	}

	public List<UserDTO> getUserArray() {
		return userArray;
	}

	public void setUserArray(List<UserDTO> userArray) {
		this.userArray = userArray;
	}
	
}
