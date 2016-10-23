package com.wanzhengchao.aspect.async.handler;

import com.alibaba.fastjson.JSONObject;
import com.wanzhengchao.aspect.async.EventHandler;
import com.wanzhengchao.aspect.async.EventModel;
import com.wanzhengchao.aspect.async.EventType;
import com.wanzhengchao.model.EntityType;
import com.wanzhengchao.model.Feed;
import com.wanzhengchao.model.Question;
import com.wanzhengchao.model.User;
import com.wanzhengchao.service.FeedService;
import com.wanzhengchao.service.FollowService;
import com.wanzhengchao.service.QuestionService;
import com.wanzhengchao.service.UserService;
import com.wanzhengchao.util.JedisAdapter;
import com.wanzhengchao.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by Administrator on 16.10.23.
 */
@Component
public class FeedHandler implements EventHandler {
    @Autowired
    FollowService followService;

    @Autowired
    UserService userService;

    @Autowired
    FeedService feedService;

    @Autowired
    JedisAdapter jedisAdapter;

    @Autowired
    QuestionService questionService;

    private String buildFeedData(EventModel model) {
        HashMap<String, String> map = new HashMap<>();
        User actor = userService.getUser(model.getActorId());
        if (actor == null) {
            return null;
        }
        map.put("userId", String.valueOf(model.getActorId()));
        map.put("userHead", actor.getHeadUrl());
        map.put("userName", actor.getName());

        if (model.getType() == EventType.COMMENT || (model.getType() == EventType.FOLLOW && model.getEntityType() == EntityType.ENTITY_QUESTION)) {
            Question question = questionService.getById(model.getEntityId());
            if(question == null){
                return null;
            }
            map.put("questionId", String.valueOf(question.getId()));
            map.put("questionTitle", question.getTitle());
            return JSONObject.toJSONString(map);
        }
        return null;
    }

    @Override
    public void doHandle(EventModel model) {
        Random random = new Random();
        model.setActorId(1 + random.nextInt(12));

        Feed feed = new Feed();
        feed.setCreatedDate(new Date());
        feed.setType(model.getType().getValue());
        feed.setUserId(model.getActorId());
        feed.setData(buildFeedData(model));

        if(feed.getData() == null){
            return;
        }
        feedService.addFeed(feed);

        List<Integer> followers = followService.getFollowers(EntityType.ENTITY_USER, model.getActorId(), Integer.MAX_VALUE);
        // 系统队列
        followers.add(0);

        for(int follower : followers){
            String timelineKey = RedisKeyUtil.getTimelineKey(follower);
            jedisAdapter.lpush(timelineKey, String.valueOf(feed.getId()));

        }


    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(new EventType[]{EventType.COMMENT, EventType.FOLLOW});
    }
}
