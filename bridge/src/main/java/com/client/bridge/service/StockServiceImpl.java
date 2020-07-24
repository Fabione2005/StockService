package com.client.bridge.service;

import java.time.LocalDate;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.client.bridge.model.dto.StockDTO;
import com.client.bridge.model.dto.UserDTO;
import com.client.bridge.model.generic.BaseResultDTO;
import com.client.bridge.model.role.Roles;
import com.client.bridge.model.utils.URLConstans;
import com.client.bridge.model.wrapper.StockResultDTO;
import com.client.bridge.model.wrapper.UserResultDTO;

@Service
public class StockServiceImpl implements StockBridgeService
{
	
	@Autowired
	RestTemplate template;
	
	private UserResultDTO userLoggedInfo;
	
	@Override
	public StockResultDTO retrieveAllStocks() {

		StockResultDTO stockWrapper = new StockResultDTO();

		UserDTO userLogged = userLoggedInfo.getUserArray() != null ? userLoggedInfo.getUserArray().stream().findFirst().get()
				: null;

		if (UserDTO.checkLoggedUser(userLogged)) {
			StockDTO[] stocks = template.getForObject(URLConstans.STOCKS_URL + "/stocks", StockDTO[].class);

			String message = null;
			String code = null;

			message = stocks.length == 0 ? "no stocks where found" : "";
			code = stocks.length == 0 ? "4154" : "0";

			stockWrapper.setStockArray(Arrays.asList(stocks));
			stockWrapper.setResponseCode(code);
			stockWrapper.setResponseDescription(message);
		} else {
			stockWrapper = new StockResultDTO(this.userLoggedInfo.getResponseCode(),this.userLoggedInfo.getResponseDescription()); 
		}

		return stockWrapper;
	}
	
	@Override
	public BaseResultDTO createStockService(StockDTO stock) {

		BaseResultDTO response = new BaseResultDTO();

		UserDTO userLogged = userLoggedInfo.getUserArray() != null ? userLoggedInfo.getUserArray().stream().findFirst().get()
				: null;

		if (UserDTO.checkLoggedUser(userLogged)) {
			if (userLogged.getRole().equalsIgnoreCase(Roles.ADMIN_ROLE)) {
				if (stock != null) {
					
					StockDTO stockFind = template.getForObject(URLConstans.STOCKS_URL + "/stocks/name/"+stock.getName(), StockDTO.class);
					
					if(stockFind == null) 
					{
						LocalDate localDate = LocalDate.now();
						stock.setLastUpdate(String.valueOf(localDate));
						template.postForEntity(URLConstans.STOCKS_URL + "/stocks", stock, String.class);
						return response;
					}
					else
					{
						response.setResponseCode("541");
						response.setResponseDescription("This Stock already exists in the system");
					}
				}
			} else {
				response.setResponseCode("540");
				response.setResponseDescription("You are not allow to create a new stock");
			}
		} else {
			response = new BaseResultDTO(this.userLoggedInfo.getResponseCode(),this.userLoggedInfo.getResponseDescription());
		}

		return response;
	}

	@Override
	public void setUserLoggValidate(UserResultDTO userLoggedResult) {
		
		if(userLoggedResult != null) 
		{
			this.userLoggedInfo = userLoggedResult;
		}
		
	}

	@Override
	public BaseResultDTO deleteStockService(int id) {
		BaseResultDTO response = new BaseResultDTO();

		UserDTO userLogged = userLoggedInfo.getUserArray() != null ? userLoggedInfo.getUserArray().stream().findFirst().get()
				: null;
		
		if (UserDTO.checkLoggedUser(userLogged)) 
		{
			if (userLogged.getRole().equalsIgnoreCase(Roles.ADMIN_ROLE))
			{
				StockDTO stockFind = template.getForObject(URLConstans.STOCKS_URL + "/stocks/"+id, StockDTO.class);
				
				if(stockFind != null) 
				{
					template.delete(URLConstans.STOCKS_URL + "/stocks/"+stockFind.getId());
				}
			}
			else
			{
				response.setResponseCode("540");
				response.setResponseDescription("You are not allow to delete a stock");
			}
		}
		else
		{
			response = new BaseResultDTO(this.userLoggedInfo.getResponseCode(),this.userLoggedInfo.getResponseDescription());
		}
		
		return response;
	}

}
