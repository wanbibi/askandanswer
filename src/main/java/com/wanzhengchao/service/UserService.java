package com.wanzhengchao.service;

import com.wanzhengchao.dao.LoginTicketDAO;
import com.wanzhengchao.dao.QuestionDAO;
import com.wanzhengchao.dao.UserDAO;
import com.wanzhengchao.model.Question;
import com.wanzhengchao.model.User;
import com.wanzhengchao.model.LoginTicket;
import com.wanzhengchao.util.WendaUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import sun.security.util.Password;

import java.util.*;

/**
 * Created by Administrator on 16.10.13.
 */
@Component
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    UserDAO userDAO;


    @Autowired
    LoginTicketDAO loginTicketDAO;

    @Transactional
    public User getUser(int id) {
        return userDAO.selectById(id);
    }


    public Map<String, Object> register(String username, String password) {
        HashMap<String, Object> map = new HashMap<>();
        if(StringUtils.isBlank(username)){
            map.put("msg","用户名不能为空");
            return map;
        }
        if(StringUtils.isBlank(password)){
            map.put("msg","密码不能为空");
            return map;
        }

        User user = userDAO.selectByName(username);
        if(user!=null){
            map.put("msg","the account has already registed!");
            return map;
        }

        user = new User();
        user.setName(username);
        user.setSalt(UUID.randomUUID().toString().substring(0,5));
        String head = String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000));
        user.setHeadUrl(head);
        user.setPassword(WendaUtil.MD5(password+user.getSalt()));
        userDAO.addUser(user);

        //登录
        String ticket = addTicket(user.getId());
        map.put("ticket", ticket);

        return map;

    }


        private String addTicket(int userId){
            LoginTicket loginTicket = new LoginTicket();
            loginTicket.setUserId(userId);
            Date date = new Date();
            date.setTime(date.getTime()+1000*3600*24);
            loginTicket.setExpired(date);
            loginTicket.setStatus(0);
            loginTicket.setTicket(UUID.randomUUID().toString().replace("-",""));
            loginTicketDAO.addTicket(loginTicket);
            return loginTicket.getTicket();
        }

    public Map<String,Object> login(String username, String password) {
        Map<String, Object> map = new HashMap<String, Object>();
        if(StringUtils.isBlank(username)){
            map.put("msg","用户名不能为空");
            return map;
        }
        if(StringUtils.isBlank(password)){
            map.put("msg","密码不能为空");
            return map;
        }

        User user = userDAO.selectByName(username);
        if(user== null){
            map.put("msg","用户名不存在");
            return map;
        }
        if(!WendaUtil.MD5(password+user.getSalt()).equals(user.getPassword())){
            map.put("msg","密码不正确");
            return map;
        }


        //登录
        String ticket = addTicket(user.getId());
        map.put("ticket", ticket);

        return map;
    }


    public void logout(String ticket) {
        loginTicketDAO.updateStatus(ticket ,1);
    }
}
