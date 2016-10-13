package com.wanzhengchao.controller;

import com.wanzhengchao.aspect.LogAspect;
import com.wanzhengchao.dao.UserDAO;
import com.wanzhengchao.model.Question;
import com.wanzhengchao.model.ViewObject;
import com.wanzhengchao.service.QuestionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 16.10.13.
 */
@Controller
public class HomeController {
    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    QuestionService questionService;
    @Autowired
    UserDAO userDAO;

    private List<ViewObject> getQuestions(int userId, int offset, int limit) {
        List<Question> latestQuestions = questionService.getLatestQuestions(userId, offset, limit);
        ArrayList<ViewObject> list = new ArrayList<>();
        for (Question question : latestQuestions) {
            ViewObject viewObject = new ViewObject();
            viewObject.set("question", question);
            viewObject.set("user", userDAO.selectById(question.getUserId()));
            list.add(viewObject);
        }
        return list;
    }

    @RequestMapping(path = "/")
    public String index(Model model) {
        model.addAttribute("vos", getQuestions(0, 0, 10));
        return "index";
    }

    @RequestMapping(path = "/user/{userid}")
    public String userIndex(Model model, @PathVariable("userid") int userid) {

        model.addAttribute("vos", getQuestions(userid, 0, 10));
        return "index";
    }
}
