package com.zxw.madaily.entity;

/**
 * Created by sony on 2015/8/1.
 */
public class Extra {
    private int long_comments;
    private int short_comments;
    private int popularity;
    private int comments;

    public Extra() {
    }

    public Extra(int long_comments, int short_comments, int popularity, int comments) {
        this.long_comments = long_comments;
        this.short_comments = short_comments;
        this.popularity = popularity;
        this.comments = comments;
    }

    public int getLong_comments() {
        return long_comments;
    }

    public void setLong_comments(int long_comments) {
        this.long_comments = long_comments;
    }

    public int getShort_comments() {
        return short_comments;
    }

    public void setShort_comments(int short_comments) {
        this.short_comments = short_comments;
    }

    public int getPopularity() {
        return popularity;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }
}
