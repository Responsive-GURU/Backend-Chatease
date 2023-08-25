package com.example.demo;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
@Service
public class UserService {
	private final UserRepository userRepository;
	@Autowired
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	public User registerUser(User user) {
		return userRepository.save(user);
	}
	
	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	public User getUserByEmail(String email) {
		User user = userRepository.findByEmail(email);
		return user;
	}
	
}

@Service
class UserPostService {

	private final UserPostRepository userPostRepository;
    @Autowired
	public UserPostService(UserPostRepository userPostRepository) {
    	this.userPostRepository = userPostRepository;
	}
    
    public List<UserPost> getUserPosts() {
    	List<UserPost> posts= userPostRepository.findAll();
    	System.out.println(posts);
    	return posts;
    }

    public UserPost addPost(UserPost userPost) {
    	userPostRepository.save(userPost);
		return userPost;
    }

	public void adddPost(UserPost newPost) {
		userPostRepository.save(newPost);		
	}
	
	public String storeImage(MultipartFile image) throws IOException {
       
        Path storagePath = Path.of("C://CHATEASE//frontend//src//media/"); 
        String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
      
        Path targetPath = storagePath.resolve(fileName);
        Files.copy(image.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

        return fileName;
    }
}

//testing
