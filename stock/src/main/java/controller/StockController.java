package controller;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import model.Stock;
import service.StockService;

@CrossOrigin(origins = "*") //permite recibir peticiones desde cualquier origen
@RestController
public class StockController {
	@Autowired
	StockService service;
	
	@GetMapping(value="stocks",produces=MediaType.APPLICATION_JSON_VALUE)
	public List<Stock> recuperarAcciones() {
		return service.retriveStocks();
	}
	
	@GetMapping(value="stocks/{id}",produces=MediaType.APPLICATION_JSON_VALUE)
	public Stock recuperarAccionPorId(@PathVariable int id) {
		return service.findStock(id);
	}
	
	@GetMapping(value="stocks/name/{name}",produces=MediaType.APPLICATION_JSON_VALUE)
	public Stock recuperarAccionPorNombre(@PathVariable String name) {
		return service.findStockByName(name);
	}
	
	@GetMapping(value="stocks/date/{lastUpdate}",produces=MediaType.APPLICATION_JSON_VALUE)
	public List<Stock> recuperarAccionesPorFecha(@PathVariable @DateTimeFormat(pattern="yyyy-MM-dd") Date lastUpdate) throws ParseException {
		return service.retrieveStockByLastUpdate(lastUpdate);
	}
	
	@GetMapping(value="stocks/developmentUpdateLess/{developmentUpdate}",produces=MediaType.APPLICATION_JSON_VALUE)
	public List<Stock> recuperarAccionesPorValorMenorQueDeCrecimiento(@PathVariable double developmentUpdate){
		return service.retrieveStockByDevelopmentUpdateLessValue(developmentUpdate);
	}
	
	@PostMapping(value="stocks",consumes=MediaType.APPLICATION_JSON_VALUE,produces=MediaType.TEXT_PLAIN_VALUE)
	public void guardarAccion(@RequestBody Stock stock) {		
		service.addStock(stock);
	}
	
	@PutMapping(value="stocks",consumes=MediaType.APPLICATION_JSON_VALUE)
	public void actualizarAccion(@RequestBody Stock stock) {		
		service.updateStock(stock);
	}
	
	@PutMapping(value="stocks/{id}",produces=MediaType.TEXT_PLAIN_VALUE)
	public String actualizarCrecimiento(@PathVariable int id) {		
		return service.updateStockDevelopment(id);
	}
	
	@DeleteMapping(value="stocks/{id}")
	public void eliminarPorId(@PathVariable int id) {
		service.deleteStock(id);
	}
	
	@PutMapping(value="stocks/developmentUpdate")
	public String actualizarIndicador() {		
		return service.updateStocksByDevelopmentUpdate();
	}
}
