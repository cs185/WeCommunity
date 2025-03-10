package edu.rice.wecommunity.dao;

import edu.rice.wecommunity.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DiscussPostMapper {

    List<DiscussPost> selectDiscussPosts(int userId, int offset, int limit);

    // @Param注解用于给参数取别名,
    // 如果只有一个参数,并且在<if>里使用,则必须加别名.
    int selectDiscussPostRows(@Param("userId") int userId);

    int insertDiscussPost(DiscussPost discussPost);

    DiscussPost selectDiscussPostById(int id);

    int updateCommentCount(int id, int commentCount);

    int updateType(int id, int type);

    int updateStatus(int id, int status);

    List<DiscussPost> findAll();

    void deleteAll();

    void batchInsertPosts(@Param("pList") List<DiscussPost> pList);

    List<String> selectImageUrls(int discussPostId);

    void insertPostImgUrl(int postId, String pathName);
}
