package com.example.demo;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.web.bind.annotation.*;
@CrossOrigin("http://localhost:3000")

@RestController
public class UserController {
	private final UserService userService;
	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}
	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@RequestBody User user) {
		Map<String, String> response = new HashMap<String, String>();
		String email = user.getEmail();
		User checkUser = userService.getUserByEmail(email);
		if(checkUser == null) {
			response.put("email", email);
			response.put("message","user Registered successfully");
			userService.registerUser(user);
			return ResponseEntity.ok(response);
		}
		else {
  
            response.put("email", email);
            response.put("message","User with email exist");
            return  ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
		
	}
	@GetMapping("/users")
	public ResponseEntity<List<User>> availableUser(){
		List<User> users = userService.getAllUsers();
			return ResponseEntity.ok(users);
	}
	
	@PostMapping("/user")
	public ResponseEntity<?> getUser(@RequestBody User user){
		String email = user.getEmail();
		User value = userService.getUserByEmail(email);
		if(value != null)
			return ResponseEntity.ok(value);
		else 
			return ((BodyBuilder) ResponseEntity.notFound()).body("User with email '" + email + "' not found.");
	
	}
	
	
	@PostMapping("/id")
	public ResponseEntity<?> getUserId(@RequestBody User user){
		String email =  user.getEmail();
		User value = userService.getUserByEmail(email);
		if(value != null) {
			return ResponseEntity.ok(value.getId().toString());
		}
		return ((BodyBuilder) ResponseEntity.notFound()).body("User with email '" + email + "' not found.");
	}
	
	@PostMapping("/login")
	public ResponseEntity<Map<String,String>> userLogin(@RequestBody User user){
		Map<String,String> response = new HashMap<String, String>();
		String email = user.getEmail();
		User value = userService.getUserByEmail(email);
		if(value != null) {
			String password = value.getPassword();
			
			if(password.equals(user.getPassword())) {
				response.put("email", email);
				response.put("message", "login successfull");
				return ResponseEntity.ok(response);
			}
			else {
				response.put("email", email);
				response.put("message","password mismatch");
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
			}
		}
		
		response.put("email", email);
		response.put("message","user not found");
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);	
	}
}
