package com.example.weibo.vo;

import lombok.Data;

@Data
public class Status implements Comparable<Status> {


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



    @Override
    public int compareTo(Status o) {
        return (int) (Long.valueOf(o.getTime())- Long.valueOf(this.getTime()));
    }
}
