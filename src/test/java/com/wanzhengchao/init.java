package com.wanzhengchao;

import com.wanzhengchao.dao.QuestionDAO;
import com.wanzhengchao.dao.UserDAO;
import com.wanzhengchao.model.Question;
import com.wanzhengchao.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Date;
import java.util.Random;

/**
 * Created by Administrator on 16.10.13.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AskandanswerApplication.class)
@WebAppConfiguration
public class init {

    @Autowired
    UserDAO userDAO;

    @Autowired
    QuestionDAO questionDAO;

    @Test
    public void contextLoads() {
        Random random = new Random();
        for(int i = 0; i<11;i++){
            User user = new User();
            user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", random.nextInt(1000)));
            user.setPassword("");
            user.setSalt("");
            user.setName(String.format("USER%d",i));
            userDAO.addUser(user);

            Question question = new Question();
            question.setCommentCount(i);
            Date date = new Date();
            date.setTime(date.getTime()+1000*3600*i);
            question.setCreatedDate(date);
            question.setContent("hehehehehehehhe");
            question.setTitle("title"+i);
            question.setUserId(i+1);
            questionDAO.addQuestion(question);
        }

    }

}
