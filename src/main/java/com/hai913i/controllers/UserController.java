package com.hai913i.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.hai913i.repositories.UserRepository;

@RestController
public class UserController {

	@Autowired
	private UserRepository userRepository;
}
