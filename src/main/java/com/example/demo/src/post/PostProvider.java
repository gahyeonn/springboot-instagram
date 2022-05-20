package com.example.demo.src.post;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.post.model.GetPostsRes;
import com.example.demo.src.user.UserProvider;
import com.example.demo.src.user.model.GetUserFeedRes;
import com.example.demo.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;
import static com.example.demo.config.BaseResponseStatus.USERS_EMPTY_USER_ID;

@Service
@RequiredArgsConstructor
public class PostProvider {
    private final PostDao postDao;
    private final UserProvider userProvider;
    private final JwtService jwtService;

    public List<GetPostsRes> retrievePosts(int userIdx) throws BaseException {
        if(userProvider.checkUserExist(userIdx) == 0) {
            throw new BaseException(USERS_EMPTY_USER_ID);
        }

        try{
            List<GetPostsRes> getPosts = postDao.selectPosts(userIdx);
            return getPosts;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkUserExist(int userIdx) throws BaseException{
        try{
            return postDao.checkUserExist(userIdx);
        } catch (Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
