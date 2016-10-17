package com.wanzhengchao.dao;

import com.sun.tracing.dtrace.ModuleAttributes;
import com.wanzhengchao.model.LoginTicket;
import com.wanzhengchao.model.User;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.logging.Log;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 16.10.15.
 */
@Mapper
@Component
public interface LoginTicketDAO {
    String TABLE_NAME = " login_ticket ";
    String INSERT_FIELDS = " user_id, ticket, expired, status ";
    String SELECT_FIELDS = " id," + INSERT_FIELDS;

    @Insert({"insert into ", TABLE_NAME, "(" + INSERT_FIELDS + ") values (#{userId},#{ticket},#{expired},#{status})"})
    int addTicket(LoginTicket ticket);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where ticket = #{ticket} and status = 0 " })
    LoginTicket selectByTicket(String ticket);

    @Update({" update ", TABLE_NAME," set status = #{status} where ticket = #{ticket}"})
    void updateStatus(@Param("ticket") String ticket, @Param("status") int status);

}
