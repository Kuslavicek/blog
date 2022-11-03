package com.example.blog.data;

import com.sun.source.doctree.AuthorTree;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface AuthorsRepository extends MongoRepository<Author,String> {
    Optional<Author> findByName(String name);

    boolean existsByName(String name);
    List<Author> findByNameStartsWith(String name);
}
