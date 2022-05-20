package com.example.demo.src.user.model;


import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class GetUserPostsRes {
    private int postIdx;
    private String postImgUrl;
}
