package com.example.demo.src.post.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetPostImgRes {
    private int postImgUrlIdx;
    private String imgUrl;
}
