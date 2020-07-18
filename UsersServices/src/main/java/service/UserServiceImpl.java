package service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dao.JpaSpring;
import model.User;

@Service
public class UserServiceImpl implements UserService
{
	
	@Autowired
	JpaSpring dao;

	@Override
	public boolean addUser(User user) {
		
		if(dao.findByUserName(user.getUserName()) == null) 
		{
			dao.save(user);
			return true;
		}
		return false;
	}

	@Override
	public List<User> retriveUsers() {
		return dao.findAll();
	}

	@Override
	public void updateUser(User user) {
		User userFind = dao.findById(user.getId()).get();
		if(userFind != null) 
		{
			dao.save(user);
		}
	}

	@Override
	public boolean deleteUser(int idUser) {
		if(dao.findById(idUser) != null) 
		{
			dao.deleteById(idUser);
			return true;
		}
		
		return false;
	}

	@Override
	public User retrieveUserById(int id) {
		return dao.findById(id).get();
	}

	@Override
	public boolean deleteUserByUserName(String userName) {
		
		User user = dao.findByUserName(userName);
		
		if(user != null) 
		{
			dao.deleteById(user.getId());
			return true;
		}
		
		return false;
	}

	@Override
	public void updateUserByUserName(User user) {
		if(dao.findByUserName(user.getUserName()) != null) 
		{
			dao.save(user);
		}
	}

	@Override
	public User retrieveUserByUserName(String userName) {
		
		User user = dao.findByUserName(userName);
		
		return user;
	}

	@Override
	public User retrieveUserLogged() {
		return dao.findByLogged(true);
	}

	@Override
	public boolean logInOrLogOutUser(int id,boolean logged,LocalDateTime lastLoggedTime) {
		 
		if(logged) 
		{
			dao.LogInUser(id,logged);
		}
		else
		{
			dao.LogOutUser(id,logged,lastLoggedTime);
		}
		
		
		return true;
	}

	@Override
	public boolean setStatusUser(int id, boolean active) {
		if(dao.findById(id) != null) 
		{
			dao.setStatusUser(id, active);
			return true;
		}
		
		return false;
	}

}
