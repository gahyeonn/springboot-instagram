package com.example.demo.src.user;


import com.example.demo.src.user.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class UserDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<GetUserRes> getUsers(){
        String getUsersQuery = "select userIdx,name,nickName,email from User";
        return this.jdbcTemplate.query(getUsersQuery,
                (rs,rowNum) -> new GetUserRes(
                        rs.getInt("userIdx"),
                        rs.getString("name"),
                        rs.getString("nickName"),
                        rs.getString("email")
                ));
    }

    public GetUserRes getUsersByEmail(String email){
        String getUsersByEmailQuery = "select userIdx,name,nickName,email from User where email=?";
        String getUsersByEmailParams = email;
        return this.jdbcTemplate.queryForObject(getUsersByEmailQuery,
                (rs, rowNum) -> new GetUserRes(
                        rs.getInt("userIdx"),
                        rs.getString("name"),
                        rs.getString("nickName"),
                        rs.getString("email")),
                getUsersByEmailParams);
    }


    //7주차 강의 내용
    public GetUserRes getUsersByIdx(int userIdx){
        String getUsersByIdxQuery = "select userIdx,name,nickName,email from User where userIdx=?";
        int getUsersByIdxParams = userIdx;
        return this.jdbcTemplate.queryForObject(getUsersByIdxQuery,
                (rs, rowNum) -> new GetUserRes(
                        rs.getInt("userIdx"),
                        rs.getString("name"),
                        rs.getString("nickName"),
                        rs.getString("email")),
                getUsersByIdxParams);
    }

    public int createUser(PostUserReq postUserReq){
        String createUserQuery = "insert into User (name, nickName, phone, email, password) VALUES (?,?,?,?,?)";
        Object[] createUserParams = new Object[]{postUserReq.getName(), postUserReq.getNickName(),postUserReq.getPhone(), postUserReq.getEmail(), postUserReq.getPassword()};
        this.jdbcTemplate.update(createUserQuery, createUserParams);

        String lastInserIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInserIdQuery,int.class);
    }

    public int checkEmail(String email){
        String checkEmailQuery = "select exists(select email from User where email = ?)";
        String checkEmailParams = email;
        return this.jdbcTemplate.queryForObject(checkEmailQuery,
                int.class,
                checkEmailParams);

    }

    public int modifyUserName(PatchUserReq patchUserReq){
        String modifyUserNameQuery = "update User set nickName = ? where userIdx = ? ";
        Object[] modifyUserNameParams = new Object[]{patchUserReq.getNickName(), patchUserReq.getUserIdx()};

        return this.jdbcTemplate.update(modifyUserNameQuery,modifyUserNameParams);
    }

    /**
     * 8주차
     */

    //유저 정보 조회
    public GetUserInfoRes selectUserInfo(int userIdx){
        String query = "select u.userIdx as userIdx, u.nickName as nickName, u.name as name, u.profileImgUrl as profileImgUrl, " +
                "u.website as website, u.introduction as introduction, " +
                "if(postCount is null, 0, followerCount) as postCount, " +
                "if(followerCount is null, 0, followerCount) as followerCount, " +
                "if(followingCount is null, 0, followingCount) as followingCount " +
                "from User as u " +
                "left join (select userIdx, count(postIdx) as postCount from Post where status = 'ACTIVE' group by userIdx) p " +
                "on p.userIdx = u.userIdx " +
                "left join (select followerIdx, count(followIdx) as followerCount " +
                "from Follow " +
                "where status = 'ACTIVE' " +
                "group by followerIdx) f1 on f1.followerIdx = u.userIdx " +
                "left join (select followeeIdx, count(followIdx) as followingCount " +
                "from Follow " +
                "where status = 'ACTIVE'" +
                "group by followeeIdx) f2 on f2.followeeIdx = u.userIdx " +
                "where u.userIdx = ? and u.status = 'ACTIVE'";
        return this.jdbcTemplate.queryForObject(query,
                (rs,rowNum) -> new GetUserInfoRes(
                        rs.getString("nickName"),
                        rs.getString("name"),
                        rs.getString("profileImgUrl"),
                        rs.getString("website"),
                        rs.getString("introduction"),
                        rs.getInt("followerCount"),
                        rs.getInt("followingCount"),
                        rs.getInt("postCount")
                ), userIdx);
    }

    //유저 게시글 조회
    public List<GetUserPostsRes> selectUserPosts(int userIdx){
        String query = "select p.postIdx as postIdx, pi.imgUrl as postImgUrl " +
                "from Post as p " +
                "join PostImgUrl as pi on pi.postIdx = p.postIdx and pi.status = 'ACTIVE' " +
                "join User as u on u.userIdx = p.userIdx " +
                "where p.status = 'ACTIVE' and u.userIdx = ? " +
                "group by p.postIdx " +
                "HAVING min(pi.postImgUrlIdx) " +
                "order by p.postIdx";
        return this.jdbcTemplate.query(query,
                (rs,rowNum) -> new GetUserPostsRes(
                        rs.getInt("postIdx"),
                        rs.getString("postImgUrl")
                ), userIdx);
    }

    public int checkUserExist(int userIdx){
        String query = "select exists(select userIdx from User where userIdx = ?)";
        return this.jdbcTemplate.queryForObject(query, int.class, userIdx);

    }
}
