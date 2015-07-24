package com.zxw.madaily.entity;

import java.util.List;

/**
 * Created by xzwszl on 7/24/2015.
 */
public class Content {

    private String body;
    private String image_source;
    private String title;
    private String image_url;
    private String share_url;
    private List<String> js;
    private List<Avatar> recommenders;
    private String ga_prefix;
    private List<Section> section;
    private int type;
    private int id;
    private List<String> css;

    public Content() {
    }

    public Content(String body, String image_source, String title, String image_url, String share_url, List<String> js, List<Avatar> recommenders, String ga_prefix, List<Section> section, int type, int id, List<String> css) {
        this.body = body;
        this.image_source = image_source;
        this.title = title;
        this.image_url = image_url;
        this.share_url = share_url;
        this.js = js;
        this.recommenders = recommenders;
        this.ga_prefix = ga_prefix;
        this.section = section;
        this.type = type;
        this.id = id;
        this.css = css;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getImage_source() {
        return image_source;
    }

    public void setImage_source(String image_source) {
        this.image_source = image_source;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getShare_url() {
        return share_url;
    }

    public void setShare_url(String share_url) {
        this.share_url = share_url;
    }

    public List<String> getJs() {
        return js;
    }

    public void setJs(List<String> js) {
        this.js = js;
    }

    public List<Avatar> getRecommenders() {
        return recommenders;
    }

    public void setRecommenders(List<Avatar> recommenders) {
        this.recommenders = recommenders;
    }

    public String getGa_prefix() {
        return ga_prefix;
    }

    public void setGa_prefix(String ga_prefix) {
        this.ga_prefix = ga_prefix;
    }

    public List<Section> getSection() {
        return section;
    }

    public void setSection(List<Section> section) {
        this.section = section;
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

    public List<String> getCss() {
        return css;
    }

    public void setCss(List<String> css) {
        this.css = css;
    }
}
