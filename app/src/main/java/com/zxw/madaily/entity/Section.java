package com.zxw.madaily.entity;

/**
 * Created by xzwszl on 7/24/2015.
 */
public class Section {

    private String thumbnail;
    private int id;
    private String name;

    public Section() {
    }

    public Section(String thumbnail, int id, String name) {
        this.thumbnail = thumbnail;
        this.id = id;
        this.name = name;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
