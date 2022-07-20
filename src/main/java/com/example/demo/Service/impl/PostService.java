package com.example.demo.Service.impl;

import com.example.demo.Entity.Post;
import com.example.demo.Repository.PostRepo;
import com.example.demo.Service.IPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class PostService implements IPostService {

    @Autowired
    private PostRepo postRepo;

    @Override
    public List<Post> getAll() {
        return postRepo.findAll();
    }
}
