package com.wanzhengchao.service;

import com.wanzhengchao.dao.QuestionDAO;
import com.wanzhengchao.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Administrator on 16.10.13.
 */
@Service
public class QuestionService {
    @Autowired
    QuestionDAO questionDAO;

    @Transactional
    public List<Question> getLatestQuestions(int userId, int offset, int limit) {
        return questionDAO.selectLatestQuestions(userId, offset, limit);
    }


}
