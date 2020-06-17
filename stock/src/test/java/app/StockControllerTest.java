package app;

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
public class StockControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
//	@Test
//	@Order(1)
	public void retrieveStocks() throws Exception {
		mockMvc.perform(get("/stocks")).andDo(print());
	}
	
	//@Test
	public void deleteStock()throws Exception {
		mockMvc.perform(delete("/stocks/2"));
	}
	
	//@Test
	public void addStock() throws Exception{
		mockMvc.perform(post("/stocks")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"name\":\"SpaceX\",\"lastUpdate\":\"2020-06-17\",\"lastPrice\":120.4,\"actualPrice\":125.7}")
				);
	}
	
//	@Test
//	@Order(0)
	public void updateStock() throws Exception{
		mockMvc.perform(put("/stocks")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"id\":7,\"name\":\"Toshiba\",\"lastUpdate\":\"2020-06-18\",\"lastPrice\":155.3,\"actualPrice\":115.2}")
				);
	}
	
	@Test
	@Order(0)
	public void retrieveStockByLessDevelopmentUpdate() throws Exception{
		mockMvc.perform(get("/stocks/developmentUpdateLess/1")
				).andDo(print());
	}



}
