package com.wanzhengchao.dao;

import com.wanzhengchao.model.Question;
import com.wanzhengchao.model.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Administrator on 16.10.13.
 */
@Mapper
@Component
public interface QuestionDAO {
    String TABLE_NAME = " question ";
    String INSERT_FIELDS = " title, content, user_id, created_date, comment_count ";
    String SELECT_FIELDS = " id," + INSERT_FIELDS;

    @Insert({"insert into ", TABLE_NAME, "(" + INSERT_FIELDS + ") values (#{title},#{content},#{userId},#{createdDate},#{commentCount})"})
    int addQuestion(Question question);

    @Select({"select ",SELECT_FIELDS," from ",TABLE_NAME," where id=#{id} "})
    Question selectById(int id);

    @Update({" update ",TABLE_NAME ,"set comment_count=#{count} where id=#{entityId}"})
    void updateCommentCount(int entityId, int count);


    List<Question> selectLatestQuestions(@Param("userId") int userId, @Param("offset") int offset,
                                         @Param("limit") int limit);


}
