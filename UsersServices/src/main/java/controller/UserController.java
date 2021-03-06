package controller;

import java.time.LocalDateTime;
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

import model.User;
import service.UserService;

@CrossOrigin(origins = "*") //permite recibir peticiones desde cualquier origen
@RestController
public class UserController {
	
	@Autowired
	UserService service;
	
	@GetMapping(value="users",produces=MediaType.APPLICATION_JSON_VALUE)
	public List<User> recuperarUsuarios() {
		return service.retriveUsers();
	}
	
	@GetMapping(value="users/{id}",produces=MediaType.APPLICATION_JSON_VALUE)
	public User recuperarUsuariosPorId(@PathVariable int id) {
		return service.retrieveUserById(id);
	}
	
	@GetMapping(value="users/search/{userName}",produces=MediaType.APPLICATION_JSON_VALUE)
	public User recuperarUsuariosPorNombreDeUsuario(@PathVariable String userName) {
		return service.retrieveUserByUserName(userName);
	}
	
	@GetMapping(value="users/logged",produces=MediaType.APPLICATION_JSON_VALUE)
	public User recuperarUsuarioLogueado() {
		return service.retrieveUserLogged();
	}
	
	@PostMapping(value="users",consumes=MediaType.APPLICATION_JSON_VALUE,produces=MediaType.TEXT_PLAIN_VALUE)
	public void guardarUsuarioNuevo(@RequestBody User User) {		
		service.addUser(User);
	}
	
	@PutMapping(value="users/logged/{id}/{logged}/{lastTimeLogged}")
	public boolean loguearODesloguearUsuario(@PathVariable int id,@PathVariable boolean logged,@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) @PathVariable LocalDateTime lastTimeLogged) {		
		return service.logInOrLogOutUser(id,logged,lastTimeLogged);
	}
	
	@PutMapping(value="users",consumes=MediaType.APPLICATION_JSON_VALUE)
	public void actualizarUsuario(@RequestBody User User) {		
		service.updateUser(User);
	}
	
	@DeleteMapping(value="users/{id}")
	public void eliminarUsuarioPorId(@PathVariable int id) {
		service.deleteUser(id);
	}
	
}
