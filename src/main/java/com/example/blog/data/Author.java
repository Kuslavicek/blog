package com.example.blog.data;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.Objects;
import java.util.Set;


@Setter
@Getter
@Document("authors")
public class Author {

    @Id
    private String id;

    private String name;
    private String hashedPasswd;
    private Rights rights;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Author author = (Author) o;
        return Objects.equals(id, author.id) && Objects.equals(name, author.name) && Objects.equals(hashedPasswd, author.hashedPasswd) && rights == author.rights;
    }


    @Override
    public String toString() {
        return name;
    }

}




