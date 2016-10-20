package com.wanzhengchao.aspect.async;

import java.util.List;

/**
 * Created by    Administrator on 16.10.20.
        */
public interface EventHandler {
    void doHandle(EventModel model);

    List<EventType> getSupportEventTypes();
}
