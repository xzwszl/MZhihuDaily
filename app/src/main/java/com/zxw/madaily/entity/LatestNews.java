package com.zxw.madaily.entity;

import java.util.List;

/**
 * Created by v-lesh on 7/22/2015.
 */
public class LatestNews {

    private String date;
    private List<Story> stories;
    private List<Story> top_stories;


    public LatestNews() {
    }

    public LatestNews(String date, List<Story> stories, List<Story> top_stories) {
        this.date = date;
        this.stories = stories;
        this.top_stories = top_stories;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<Story> getStories() {
        return stories;
    }

    public void setStories(List<Story> stories) {
        this.stories = stories;
    }

    public List<Story> getTop_stories() {
        return top_stories;
    }

    public void setTop_stories(List<Story> top_stories) {
        this.top_stories = top_stories;
    }
}
