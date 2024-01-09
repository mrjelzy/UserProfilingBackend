package com.hai913i.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hai913i.models.User;
import com.hai913i.repositories.UserRepository;

@RestController
public class UserController {

	@Autowired
	private UserRepository userRepository;
	
	@RequestMapping("/users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
	
	@RequestMapping("/users.html")
	public String showAllUsers()
	{
		String result = "<h1>Liste des utilisateurs</h1> <ul>";
		
		for (User u : userRepository.findAll())
		{
			result += "<li>" + u.toString() + "</li>";
		}
		
		return result + "</ul>";
	}
}
