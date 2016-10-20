package com.wanzhengchao.aspect.async.handler;

import com.wanzhengchao.aspect.async.EventHandler;
import com.wanzhengchao.aspect.async.EventModel;
import com.wanzhengchao.aspect.async.EventType;
import com.wanzhengchao.util.MailSender;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by nowcoder on 2016/7/30.
 */
@Component
public class LoginExceptionHandler implements EventHandler {

    @Autowired
    MailSender mailSender;

    @Override
    public void doHandle(EventModel model) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("username",model.getExt("username"));
        mailSender.sendWithHTMLTemplate(model.getExt("email"), "登陆IP异常", "mails/login_exception.html", map);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LOGIN);
    }
}
