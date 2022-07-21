package com.example.demo.api;

import com.example.demo.Service.impl.PostService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class Post {
    @Mock
    PostService postService;

}
