package com.client.bridge;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
class ClientServiceApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
//	@Order(1)
	public void retrieveStocks() throws Exception {
		mockMvc.perform(get("/stocks")).andDo(print());
	}

	@Test
//	@Order(1)
	public void retrieveAllUsers() throws Exception {
		mockMvc.perform(get("/users/all")).andDo(print());
	}
	
	@Test
//	@Order(1)
	public void loggingService() throws Exception {
		mockMvc.perform(get("/users/login/Fabione2005/123456")).andDo(print());
	}

	@Test
	public void addUser() throws Exception {
		mockMvc.perform(post("/users/addUser").contentType(MediaType.APPLICATION_JSON).content(
				"{\"fullName\":\"Bobureño Lokis\",\"userName\":\"elmomoerco\",\"email\":\"Lokitoys@gmail.com\""
						+ ",\"password\":\"123456\",\"createdDate\":\"2020-06-20\",\"role\":\"USER\"}")).andDo(print());
	}

	@Test
	public void addStock() throws Exception {
		mockMvc.perform(post("/stocks/addStock").contentType(MediaType.APPLICATION_JSON).content(
				"{\"name\":\"AOC\",\"lastUpdate\":\"2020-07-25\",\"lastPrice\":45,\"actualPrice\":32}")).andDo(print());
	}
	
	@Test
//	@Order(0)
	public void logOutService() throws Exception{
		mockMvc.perform(put("/users/logout")).andDo(print());
	}
	
	@Test
//	@Order(0)
	public void deleteStockService() throws Exception{
		mockMvc.perform(delete("/stocks/7")).andDo(print());
	}
	
	@Test
//	@Order(0)
	public void deleteUserService() throws Exception{
		mockMvc.perform(delete("/user/9")).andDo(print());
	}

}
