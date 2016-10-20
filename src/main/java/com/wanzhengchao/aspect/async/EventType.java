package com.wanzhengchao.aspect.async;

/**
 * Created by Administrator on 16.10.20.
 */
public enum EventType {
    LIKE(0),
    COMMENT(1),
    LOGIN(2),
    MAIL(3);

    private int value;
    EventType(int value){
        this.value=value;
    }
    public int getValue(){
        return value;
    }
}