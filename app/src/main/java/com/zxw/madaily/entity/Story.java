package com.zxw.madaily.entity;

import java.util.List;

/**
 * Created by xzwszl on 7/22/2015.
 */
public class Story {

    private String title;
    private String ga_prefix;
    private List<String> images;
    private String image;
    private int type;
    private int id;

    public Story() {
    }

    public Story(String title, String ga_prefix, List<String> images, String image, int type, int id) {
        this.title = title;
        this.ga_prefix = ga_prefix;
        this.images = images;
        this.image = image;
        this.type = type;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGa_prefix() {
        return ga_prefix;
    }

    public void setGa_prefix(String ga_prefix) {
        this.ga_prefix = ga_prefix;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
