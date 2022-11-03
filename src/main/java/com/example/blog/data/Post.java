package com.example.blog.data;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Getter
@Setter
@Document("posts")
public class Post{
    @Id
    private String id;
    private String authorId;
    private String title;
    private String content;
    private Date date;






}
