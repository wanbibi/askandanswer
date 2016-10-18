package com.wanzhengchao.controller;

import com.sun.org.apache.xpath.internal.operations.Mod;
import com.wanzhengchao.model.HostHolder;
import com.wanzhengchao.model.Message;
import com.wanzhengchao.model.User;
import com.wanzhengchao.model.ViewObject;
import com.wanzhengchao.service.MessageService;
import com.wanzhengchao.service.SensitiveService;
import com.wanzhengchao.service.UserService;
import com.wanzhengchao.util.WendaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 16.10.17.
 */
@Controller
public class MessageController {
    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    MessageService messageService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    UserService userService;


    @RequestMapping(path = {"/msg/list"})
    public String getConversationList(Model model) {

        if (hostHolder.getUser() == null) {
            return "redirect:/reglogin";
        }
        int userId = hostHolder.getUser().getId();
        List<Message> conversationList = messageService.getConversationList(userId, 0, 10);
        ArrayList<ViewObject> conversations = new ArrayList<>();
        for (Message message : conversationList) {
            ViewObject vo = new ViewObject();
            vo.set("conversation", message);
            int targetId = message.getFromId() == userId ? message.getToId() : message.getFromId();
            vo.set("user", userService.getUser(targetId));
            vo.set("unread", messageService.getConversationUnreadCount(userId, message.getConversationId()));
            conversations.add(vo);
        }
        model.addAttribute("conversations", conversations);

        return "letter";
    }

    @RequestMapping(path = {"/msg/detail"})
    public String getConversationDetail(Model model, @RequestParam("conversationId") String conversationId) {
        try {

            List<Message> list = messageService.getConversationDetail(conversationId, 0, 10);
            ArrayList<ViewObject> messages = new ArrayList<>();
            for (Message message : list) {
                ViewObject vo = new ViewObject();
                vo.set("message", message);
                vo.set("user", userService.getUser(message.getFromId()));
                messages.add(vo);
            }
            model.addAttribute("messages", messages);
        } catch (Exception e) {
            logger.error("detail Failed");
        }
        return "letterDetail";
    }

    @RequestMapping(path = {"/msg/addMessage"}, method = {RequestMethod.POST})
    @ResponseBody
    public String addMessage(@RequestParam("toName") String toName,
                             @RequestParam("content") String content) {

        try {

            if (hostHolder.getUser() == null) {
                return WendaUtil.getJSONString(999, " 未登录");
            }
            User user = userService.selectByName(toName);
            if (user == null) {
                return WendaUtil.getJSONString(1, "no such User");
            }
            Message message = new Message();
            message.setCreatedDate(new Date());
            message.setContent(content);
            message.setFromId(hostHolder.getUser().getId());
            message.setToId(user.getId());
            messageService.addMessage(message);
            return WendaUtil.getJSONString(0);
        } catch (Exception e) {
            logger.error("send message failed" + e.getMessage());
            return WendaUtil.getJSONString(1, "send message failed");
        }

    }
}
