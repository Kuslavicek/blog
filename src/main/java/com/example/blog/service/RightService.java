package com.example.blog.service;

import ch.qos.logback.core.read.ListAppender;
import com.example.blog.data.Author;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Getter
@Setter
@Service
public class RightService {
    private boolean isAdmin;
    private boolean isUser;
    private Author author;

    RightService(){
        isAdmin = false;
        isUser = false;
        author = null;
    }
}
