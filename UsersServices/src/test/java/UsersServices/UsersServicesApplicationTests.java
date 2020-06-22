package UsersServices;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
class UsersServicesApplicationTests {

	@Autowired
	private MockMvc mockMvc;
	
	@Test
	@Order(1)
	public void retrieveUsers() throws Exception {
		mockMvc.perform(get("/users")).andDo(print());
	}
	
	@Test
	public void retrieveUserByName() throws Exception {
		mockMvc.perform(get("/users/search/Fabione2005")).andDo(print());
	}
	
	@Test
	public void retrieveUserLogged() throws Exception {
		mockMvc.perform(get("/users/logged")).andDo(print());
	}
	
	@Test
	@Order(0)
	public void deleteUser()throws Exception {
		mockMvc.perform(delete("/users/4"));
	}
	
	@Test
	public void addUser() throws Exception{
		mockMvc.perform(post("/users")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"fullName\":\"Robert Baratheon\",\"userName\":\"RobertiniB\",\"email\":\"robertiene123@gmail.com\""
						+ ",\"password\":\"123456\",\"createdDate\":\"2020-06-20\",\"role\":\"ADMIN\"}")
				);
	}
	
	@Test
	public void logInOrLogOutUser() throws Exception{
		mockMvc.perform(put("/users/logged/1/true"));
	}
	
	@Test
//	@Order(0)
	public void updateUser() throws Exception{
		mockMvc.perform(put("/users")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"id\":\"3\",\"fullName\":\"Robert Baratheon\",\"userName\":\"RobertiniAAAA\",\"email\":\"robertiene123@gmail.com\""
						+ ",\"password\":\"123456\",\"createdDate\":\"2020-06-20\",\"role\":\"ADMIN\"}")
				);
	}
	

}
