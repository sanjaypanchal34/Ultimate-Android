package com.sk.ultimateplayerhq.models;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.Objects;

public class SessionModel implements Serializable {

    @SerializedName("post_title")
    private String post_title;

    @SerializedName("post_content")
    private String post_content;

    @SerializedName("guid")
    private String guid;

    @SerializedName("thumbnail_url")
    private String thumbnail_url;

    @SerializedName("ID")
    private int ID;

    public SessionModel(int ID) {
        this.ID = ID;
    }

    public static SessionModel fromJson(JSONObject object) {
        return new Gson().fromJson(object.toString(), SessionModel.class);
    }


    public static SessionModel fromString(String user) {
        return new Gson().fromJson(user, SessionModel.class);
    }

    public static String toString(SessionModel model) {
        return new Gson().toJson(model);
    }


    public String getPost_title() {
        return post_title;
    }

    public void setPost_title(String post_title) {
        this.post_title = post_title;
    }

    public String getPost_content() {
        return post_content;
    }

    public void setPost_content(String post_content) {
        this.post_content = post_content;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getThumbnail_url() {
        return thumbnail_url;
    }

    public void setThumbnail_url(String thumbnail_url) {
        this.thumbnail_url = thumbnail_url;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SessionModel)) return false;
        SessionModel that = (SessionModel) o;
        return getID() == that.getID();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getID());
    }
}
