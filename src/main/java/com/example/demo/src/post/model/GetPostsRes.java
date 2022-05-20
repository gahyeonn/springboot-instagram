package com.example.demo.src.post.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GetPostsRes {
    private int postIdx;
    private int userIdx;
    private String nickName;
    private String profileImgUrl;
    private String content;
    private int postLikeCount;
    private int commentCount;
    private String updatedAt;
    private String likeOrNot; //글 좋아요 여부
    private List<GetPostImgRes> imgs;
}
