package com.wanzhengchao.aspect.async.handler;

import com.wanzhengchao.aspect.async.EventHandler;
import com.wanzhengchao.aspect.async.EventModel;
import com.wanzhengchao.aspect.async.EventType;
import com.wanzhengchao.service.SearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 16.10.27.
 */
@Component
public class AddQuestionHandler implements EventHandler {
    private static final Logger logger = LoggerFactory.getLogger(AddQuestionHandler.class);
    @Autowired
    SearchService searchService;

    @Override
    public void doHandle(EventModel model) {
        try {
            searchService.indexQuestion(model.getEntityId(),model.getExt("title"),model.getExt("content"));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("add question index failed");
        }
    }

    @Override
    public List<EventType> getSupportEventTypes() {
       return Arrays.asList(EventType.ADD_QUESTION);
    }
}
