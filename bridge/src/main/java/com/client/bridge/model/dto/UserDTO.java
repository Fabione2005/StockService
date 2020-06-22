package com.client.bridge.model.dto;

import java.time.LocalDate;

public class UserDTO 
{
	private int id;
	
	private String fullName;
	
	private String userName;
	
	private String email;
	
	private String password;
	
	private LocalDate createdDate;
	
	private String createdby;
	
	private String role;
	
	private boolean logged;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isLogged() {
		return logged;
	}

	public void setLogged(boolean logged) {
		this.logged = logged;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullname) {
		this.fullName = fullname;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public LocalDate getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDate createdDate) {
		this.createdDate = createdDate;
	}

	public String getCreatedby() {
		return createdby;
	}

	public void setCreatedby(String createdby) {
		this.createdby = createdby;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
	
	public void setCreatedDateAndCreatedBy(LocalDate date, String createdby) 
	{
		this.createdDate = date;
		this.createdby = createdby;
	}
}
