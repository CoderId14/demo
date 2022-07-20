package com.example.demo.api.post;


import com.example.demo.Entity.Post;
import com.example.demo.Service.impl.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/post")
public class PostController {
    @Autowired
    private PostService postService;
    @GetMapping
    ResponseEntity<?> getAllPost(){
        List<Post> list = postService.getAll();
        return ResponseEntity.ok(list);
    }
}
