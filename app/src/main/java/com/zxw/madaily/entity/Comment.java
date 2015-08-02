package com.zxw.madaily.entity;

/**
 * Created by sony on 2015/8/2.
 */
public class Comment {

    private String author;
    private int id;
    private String content;
    private String likes;
    private long time;
    private String avatar;

    public Comment() {
    }

    public Comment(String author, int id, String content, String likes, long time, String avatar) {
        this.author = author;
        this.id = id;
        this.content = content;
        this.likes = likes;
        this.time = time;
        this.avatar = avatar;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
