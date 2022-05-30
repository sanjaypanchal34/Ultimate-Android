package com.sk.ultimateplayerhq.models;

import java.io.Serializable;

public class HighlightModel implements Serializable {

    String type;
    String url;
    String v_thumbnail_url;
    int user_id;
    String name;
    int highlight_id;
    String user_role;
    String is_process;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getV_thumbnail_url() {
        return v_thumbnail_url;
    }

    public void setV_thumbnail_url(String v_thumbnail_url) {
        this.v_thumbnail_url = v_thumbnail_url;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHighlight_id() {
        return highlight_id;
    }

    public void setHighlight_id(int highlight_id) {
        this.highlight_id = highlight_id;
    }

    public String getUser_role() {
        return user_role;
    }

    public void setUser_role(String user_role) {
        this.user_role = user_role;
    }

    public String getIs_process() {
        return is_process;
    }

    public void setIs_process(String is_process) {
        this.is_process = is_process;
    }
}
