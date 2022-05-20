package com.example.demo.src.user.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Data
@AllArgsConstructor
public class GetUserFeedRes {
    private boolean _isMyFeed;
    private GetUserInfoRes getUserInfo;
    private List<GetUserPostsRes> getUserPosts;
}
