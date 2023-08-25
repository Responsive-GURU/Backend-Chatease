package com.example.demo;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
@CrossOrigin("http://localhost:3000")

@RestController
public class UserController {
	private final UserService userService;
	private final UserPostService userPostService;
	
	@Autowired
	public UserController(UserService userService, UserPostService userPostService) {
		this.userService = userService;
		this.userPostService = userPostService;
	}
	
	@PostMapping("/chatease/register")
	public ResponseEntity<Map<String, String>> registerUser(@RequestBody User user) {
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
			return  ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
	}
	
	
	@PostMapping("/id")
	public ResponseEntity<String> getUserId(@RequestBody User user){
		String email =  user.getEmail();
		User value = userService.getUserByEmail(email);
		if(value != null) {
			return ResponseEntity.ok(value.getId().toString());
		}
		return  ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
	}
	
	@PostMapping("/chatease/login")
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
	
	@PostMapping("/chatease/userpos")
	public ResponseEntity<?> newPost(@RequestBody UserPost userPost){
		String email = userPost.getEmail();
		User user = userService.getUserByEmail(email);
		ObjectId userId = user.getId();
		userPost.setUserId(userId);
		userPost.setEmail(null);
		userPostService.addPost(userPost);
		return ResponseEntity.ok(userPost);
		
	}
		
	
	@GetMapping("/chatease/allpost")
    public ResponseEntity<List<UserPost>> getAllPost() {   	
        List<UserPost>  posts = userPostService.getUserPosts();
		return ResponseEntity.ok(posts);
     
    }
	
	
	
	
	
	@PostMapping("/chatease/userpost")
    public ResponseEntity<String> addPost(@RequestParam String caption,
    									 @RequestParam String date,
                                         @RequestParam MultipartFile image,
                                         @RequestParam String email) {
        try {
        	User user = userService.getUserByEmail(email);
        	ObjectId userId = user.getId();
        	String userName = user.getUserName();
        	
            String imageUrl = userPostService.storeImage(image); // Store image and get URL
            
            UserPost newPost = new UserPost();
//            newPost.set
            newPost.setDate(date);
            newPost.setCaption(caption);
            newPost.setUserName(userName);
            newPost.setImage(imageUrl);
            
            
//            newPost.setEmail(email);
            newPost.setUserId(userId);
            userPostService.adddPost(newPost);
            
            return ResponseEntity.status(HttpStatus.CREATED).body("Post added successfully");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding post");
        }
    }
	
	@PostMapping("/chatease/like")
	public ResponseEntity<String> userLike(@RequestBody String email){
		
		return null;
		
	}

//    private String storeImage(MultipartFile image) throws IOException {
//        // Define the storage directory or use your preferred storage solution
//        Path storagePath = Path.of("C://CHATEASE//frontend//src//media/"); // Replace with actual path
//
//        // Generate a unique file name
//        String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
//
//        // Combine the storage path with the file name
//        Path targetPath = storagePath.resolve(fileName);
//
//        // Copy the uploaded image to the target storage location
//        Files.copy(image.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
//
//        // Return the URL to the stored image (you can format it as needed)
//        return fileName;
//    }
	
	
}
