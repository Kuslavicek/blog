package com.example.blog.data;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;
@Repository
public interface PostRepository extends MongoRepository<Post,String> {

    Set<Post> findAllByAuthorId(String authorId);
}
