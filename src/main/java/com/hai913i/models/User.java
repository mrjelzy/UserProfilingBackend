package com.hai913i.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

public class User {
	@Id
	@GeneratedValue
	private long id;
	private String name;
	private String email;
	private String birthday;
	@JsonIgnore
	private String password;
	
	public User() {}
	
	public User(long id, String name, String email, String birthday, String password) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.birthday = birthday;
		this.password = password;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", email=" + email + ", birthday=" + birthday + ", password="
				+ password + "]";
	}

}
