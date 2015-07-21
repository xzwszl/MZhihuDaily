package com.zxw.madaily.entity;

import java.util.List;

/**
 * Created by sony on 2015/7/21.
 */
public class DailyTheme {

    private int limit;

    int[] subscribed;

    List<Theme> others;

    public DailyTheme() {
    }

    public DailyTheme(int limit, int[] subscribed, List<Theme> others) {
        this.limit = limit;
        this.subscribed = subscribed;
        this.others = others;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int[] getSubscribed() {
        return subscribed;
    }

    public void setSubscribed(int[] subscribed) {
        this.subscribed = subscribed;
    }

    public List<Theme> getOthers() {
        return others;
    }

    public void setOthers(List<Theme> others) {
        this.others = others;
    }
}
