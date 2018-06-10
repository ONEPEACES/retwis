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


    /**
     * o1 - o2 是升序(自然排序)
     * o2 - o1 是降序
     * @param o
     * @return
     */
    @Override
    public int compareTo(Status o) {
        return (int) (Long.valueOf(o.getTime())- Long.valueOf(this.getTime()));
    }
}
