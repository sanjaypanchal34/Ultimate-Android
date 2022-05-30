package com.sk.ultimateplayerhq.models;

import java.io.Serializable;

public class ChatModel implements Serializable {
    String message;
    String name;
    String time;
    String type;
    String url;
    String jersey_number;
    String user_role;
    int user_id;
    String v_thumbnail_url;

    public ChatModel(String message, String name, String time, String type, String url, int user_id, String v_thumbnail_url, String jersey_number, String user_role) {
        this.message = message;
        this.name = name;
        this.time = time;
        this.type = type;
        this.url = url;
        this.user_id = user_id;
        this.v_thumbnail_url = v_thumbnail_url;
        this.jersey_number = jersey_number;
        this.user_role = user_role;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

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

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getV_thumbnail_url() {
        return v_thumbnail_url;
    }

    public void setV_thumbnail_url(String v_thumbnail_url) {
        this.v_thumbnail_url = v_thumbnail_url;
    }

    public String getJersey_number() {
        return jersey_number==null?"0":jersey_number.isEmpty()?"0":jersey_number;
    }

    public void setJersey_number(String jersey_number) {
        this.jersey_number = jersey_number;
    }

    public String getUser_role() {
        return user_role;
    }

    public void setUser_role(String user_role) {
        this.user_role = user_role;
    }
}
