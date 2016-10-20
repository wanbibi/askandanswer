package com.wanzhengchao.aspect.async.handler;

import com.wanzhengchao.aspect.async.EventHandler;
import com.wanzhengchao.aspect.async.EventModel;
import com.wanzhengchao.aspect.async.EventType;
import com.wanzhengchao.model.Message;
import com.wanzhengchao.model.User;
import com.wanzhengchao.service.MessageService;
import com.wanzhengchao.service.UserService;
import com.wanzhengchao.util.WendaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 16.10.20.
 */
@Component
public class LikeHandler implements EventHandler {
    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;
    @Override
    public void doHandle(EventModel model) {
        Message message = new Message();
        message.setFromId(WendaUtil.SYSTEM_USERID);
        message.setToId(model.getEntityOwnerId());
        message.setCreatedDate(new Date());
        User user = userService.getUser(model.getActorId());
        message.setContent("用户" + user.getName()+"赞了你的评论"+model.getExt("questionId"));

        messageService.addMessage(message);

    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LIKE);
    }
}
