package com.zxw.madaily.entity;

/**
 * Created by xzwszl on 7/23/2015.
 */
public class Editor {

    private String url;
    private String bio;
    private int id;
    private String avatar;
    private String name;

    public Editor() {
    }

    public Editor(String url, String bio, int id, String avatar, String name) {
        this.url = url;
        this.bio = bio;
        this.id = id;
        this.avatar = avatar;
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
