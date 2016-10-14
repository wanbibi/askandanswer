package com.wanzhengchao.service;

import com.wanzhengchao.dao.QuestionDAO;
import com.wanzhengchao.dao.UserDAO;
import com.wanzhengchao.model.Question;
import com.wanzhengchao.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Administrator on 16.10.13.
 */
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    UserDAO userDAO;

    @Transactional
    public User getUser(int id){
        return userDAO.selectById(id);
    }


}
