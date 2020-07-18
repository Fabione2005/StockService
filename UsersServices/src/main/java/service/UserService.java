package service;

import java.time.LocalDateTime;
import java.util.List;

import model.User;

public interface UserService {
	boolean addUser(User user);
	List<User> retriveUsers();
	void updateUser(User user);
	void updateUserByUserName(User user);
	boolean deleteUser(int idUser);
	boolean deleteUserByUserName(String userName);
	User retrieveUserById(int id);
	User retrieveUserByUserName(String userName);
	User retrieveUserLogged();
	boolean logInOrLogOutUser(int id,boolean logged,LocalDateTime lastLoggedTime);
}
