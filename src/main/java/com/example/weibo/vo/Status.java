package com.example.weibo.vo;

import lombok.Data;

@Data
public class Status {


    public Status(String content, String time, String username) {
        this.content = content;
        this.time = time;
        this.username = username;
    }


    public Status() {
    }

    private String content;
    private String time;
    /**
     * the status publisher
     */
    private String username;
}
