package com.hwy.example.entity;

import java.io.Serializable;

/**
 * 作者: hewenyu
 * 日期: 2018/11/1 10:34
 * 说明:
 */
public class NoticeBean implements Serializable {

    private String name;

    private String content;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "NoticeBean{" +
                "name='" + name + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
