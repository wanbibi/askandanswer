package com.wanzhengchao.controller;

import com.wanzhengchao.aspect.LogAspect;
import com.wanzhengchao.model.HostHolder;
import com.wanzhengchao.model.Question;
import com.wanzhengchao.service.QuestionService;
import com.wanzhengchao.service.UserService;
import com.wanzhengchao.util.WendaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * Created by Administrator on 16.10.16.
 */
@Controller
public class QuestionController {

    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    @Autowired
    QuestionService questionService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    UserService userService;

    @RequestMapping(value = "/question/add", method = RequestMethod.POST)
    @ResponseBody
    public String addQuestion(@RequestParam("title") String title, @RequestParam("content") String content) {
        try {
            Question question = new Question();
            question.setContent(content);
            question.setTitle(title);
            question.setCreatedDate(new Date());
            if (hostHolder.getUser() == null) {
                question.setUserId(WendaUtil.ANONYMOUS_USERID);
            } else {
                question.setUserId(hostHolder.getUser().getId());
            }
            if (questionService.addQuestion(question) > 0) {
                return WendaUtil.getJSONString(0);
            }
        } catch (Exception e) {
            logger.error("add failed" + e.getMessage());
        }
        return WendaUtil.getJSONString(1, "faild");

    }


    @RequestMapping(value = "/question/{qid}", method = RequestMethod.GET)
    public String questionDetail(Model model, @PathVariable("qid") int qid) {
        Question question = questionService.getById(qid);
        model.addAttribute("question", question);
        model.addAttribute("user", userService.getUser(question.getUserId()));
        return "detail";

    }


}
