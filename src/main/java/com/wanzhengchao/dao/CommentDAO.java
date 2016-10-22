package com.wanzhengchao.dao;

import com.wanzhengchao.model.Comment;
import com.wanzhengchao.model.LoginTicket;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.ParameterMap;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Administrator on 16.10.17.
 */
@Component
@Mapper
public interface CommentDAO {
    String TABLE_NAME = " comment ";
    String INSERT_FIELDS = " user_id, content, created_date, entity_id, entity_type, status ";
    String SELECT_FIELDS = " id," + INSERT_FIELDS;

    @Insert({"insert into ", TABLE_NAME, "(" + INSERT_FIELDS + ") values (#{userId},#{content},#{createdDate},#{entityId},#{entityType},#{status})" })
    int addComment(Comment comment);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where entity_type = #{entityType} and entity_id = #{entityId}" })
    List<Comment> selectByEntity(@Param("entityId") int entityId, @Param("entityType") int entityType);

    @Update({" update ", TABLE_NAME, " set status = #{status} where entity_id=#{entityId} and entity_type=#{entityType}" })
    void updateStatus(@Param("status") int status, @Param("entityId") int entityId, @Param("entityType") int entityType);

    @Select({"select count(id) from ", TABLE_NAME, " where entity_id=#{entityId} and entity_type=#{entityType}" })
    int getCommentCount(@Param("entityId") int entityId, @Param("entityType") int entityType);


    @Select({"select", SELECT_FIELDS ,"from ", TABLE_NAME, " where id=#{id}" })
    Comment getCommentById(int id);

    @Select({"select count(id) from ", TABLE_NAME, " where user_id=#{userId}"})
    int getUserCommentCount(int userId);
}
