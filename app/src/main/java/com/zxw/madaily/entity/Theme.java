package com.zxw.madaily.entity;

/**
 * Created by v-lesh on 7/21/2015.
 */
public class Theme {

    private int color;
    private String thumbnail;
    private String description;
    private int id;
    private String name;

    public Theme() {
    }

    public Theme(String name, int color, String thumbnail, String description, int id) {
        this.name = name;
        this.color = color;
        this.thumbnail = thumbnail;
        this.description = description;
        this.id = id;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
