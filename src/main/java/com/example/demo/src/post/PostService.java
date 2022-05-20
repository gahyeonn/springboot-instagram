package com.example.demo.src.post;

import com.example.demo.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final PostProvider postProvider;
    private final PostDao postDao;
    private final JwtService jwtService;
}
