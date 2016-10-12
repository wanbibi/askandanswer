package com.wanzhengchao.model;

/**
 * Created by Administrator on 16.10.12.
 */
public class User {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User(String name){
        this.name = name;

    }

    public String getDesc(){
        return "this is " + name;
    }

}
