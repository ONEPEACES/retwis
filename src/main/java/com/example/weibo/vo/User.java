package com.example.weibo.vo;

import lombok.Data;

@Data
public class User {
    public User(String username){
        this.username = username;
    }

    public User() {
    }

    private String username;
    private String password;
}
