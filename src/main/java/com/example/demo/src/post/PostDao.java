package com.example.demo.src.post;

import com.example.demo.src.post.model.GetPostImgRes;
import com.example.demo.src.post.model.GetPostsRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class PostDao {
    private JdbcTemplate jdbcTemplate;
    private List<GetPostImgRes> getPostImgRes;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<GetPostsRes> selectPosts(int userIdx) {
        String query = "SELECT p.postIdx as postIdx, " +
                "u.userIdx as userIdx, " +
                "u.nickName as nickName, " +
                "u.profileImgUrl as profileImgUrl, " +
                "p.content as content, " +
                "IF(postLikeCount is null, 0, postLikeCount) as postLikeCount, " +
                "IF(commentCount is null, 0, commentCount) as commentCount, " +
                "case " +
                "when timestampdiff(second, p.updatedAt, current_timestamp) < 60 " +
                "then concat(timestampdiff(second, p.updatedAt, current_timestamp), '초 전') " +
                "when timestampdiff(minute , p.updatedAt, current_timestamp) < 60 " +
                "then concat(timestampdiff(minute, p.updatedAt, current_timestamp), '분 전') " +
                "when timestampdiff(hour , p.updatedAt, current_timestamp) < 24 " +
                "then concat(timestampdiff(hour, p.updatedAt, current_timestamp), '시간 전') " +
                "when timestampdiff(day , p.updatedAt, current_timestamp) < 365 " +
                "then concat(timestampdiff(day, p.updatedAt, current_timestamp), '일 전') " +
                "else timestampdiff(year , p.updatedAt, current_timestamp) " +
                "end as updatedAt, " +
                "IF(pl.status = 'ACTIVE', 'Y', 'N') as likeOrNot " +
                "FROM Post as p " +
                "join User as u on u.userIdx = p.userIdx " +
                "left join (select postIdx, userIdx, count(postLikeidx) as postLikeCount from PostLike WHERE status = 'ACTIVE' group by postIdx) plc on plc.postIdx = p.postIdx " +
                "left join (select postIdx, count(commentIdx) as commentCount from Comment WHERE status = 'ACTIVE' group by postIdx) c on c.postIdx = p.postIdx " +
                "left join Follow as f on f.followeeIdx = p.userIdx and f.status = 'ACTIVE' " +
                "left join PostLike as pl on pl.userIdx = f.followerIdx and pl.postIdx = p.postIdx " +
                "WHERE f.followerIdx = ? and p.status = 'ACTIVE' " +
                "group by p.postIdx";
        return jdbcTemplate.query(query,
                (rs, rowNum) -> new GetPostsRes(
                        rs.getInt("postIdx"),
                        rs.getInt("userIdx"),
                        rs.getString("nickName"),
                        rs.getString("profileImgUrl"),
                        rs.getString("content"),
                        rs.getInt("postLikeCount"),
                        rs.getInt("commentCount"),
                        rs.getString("updatedAt"),
                        rs.getString("likeOrNot"),
                        getPostImgRes = this.jdbcTemplate.query("select pi.PostImgUrlIdx, pi.imgUrl " +
                                "from PostImgUrl as pi " +
                                "join Post as p on p.postIdx = pi.postIdx " +
                                "where pi.status = 'ACTIVE' and p.postIdx = ?",
                        (rk, rownum) -> new GetPostImgRes(
                                rk.getInt("postImgUrlIdx"),
                                rk.getString("imgUrl")
                        ), rs.getInt("postIdx")
                        )
                ), userIdx);
    }

    public int checkUserExist(int userIdx){
        String query = "select exists(select userIdx from User where userIdx = ?)";
        return this.jdbcTemplate.queryForObject(query, int.class, userIdx);

    }
}
