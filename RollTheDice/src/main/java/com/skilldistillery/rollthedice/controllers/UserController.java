package com.skilldistillery.rollthedice.controllers;

import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skilldistillery.rollthedice.entities.User;
import com.skilldistillery.rollthedice.services.UserService;

@RestController
@RequestMapping("api")
@CrossOrigin({"*", "http://localhost:4300"})
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@GetMapping("users")
	public List<User> findAllUsers(HttpServletRequest req, HttpServletResponse res, Principal principal) {
		return userService.findAllUsers(principal.getName());
	}
	
	@GetMapping("users/{userId}")
	public User findUserById(HttpServletRequest req, HttpServletResponse res, Principal principal, @PathVariable int userId) {
		User resultUser = userService.findUserById(principal.getName(), userId);
		if (resultUser == null) {
			res.setStatus(404);
		}
		return resultUser;
	}
	
	@PostMapping("users")
	public User createUser(HttpServletRequest req, HttpServletResponse res, Principal principal, @RequestBody User user) {
		try {
			user = userService.createUser(principal.getName(), user);
			res.setStatus(201);
			StringBuffer url = req.getRequestURL();
			url.append("/").append(user.getId());
			res.setHeader("Location", url.toString());
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Invalid JSON for New User");
			res.setStatus(400);
		}
		return user;
	}
	
	@PutMapping("users/{userId}")
	public User update(HttpServletRequest req, HttpServletResponse res, Principal principal, @PathVariable int userId, @RequestBody User user) {
		User updatedUser = userService.updateUser(principal.getName(), user);
		if (updatedUser != null) {
			res.setStatus(201);
		}
		return updatedUser;
	}
	
	@DeleteMapping("users/{userId}")
	public void destroyUser(HttpServletRequest req, HttpServletResponse res, Principal principal, @PathVariable int userId) {
		userService.destroyUser(principal.getName(), userId);
	}

}
