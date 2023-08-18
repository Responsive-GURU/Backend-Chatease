package com.example.demo;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, ObjectId> {
    User findByEmail(String email);
}


interface UserPostRepository extends MongoRepository<UserPost, ObjectId> {

	List<UserPost> findPostById(ObjectId userId);

}