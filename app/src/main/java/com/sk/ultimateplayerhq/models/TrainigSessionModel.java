package com.sk.ultimateplayerhq.models;

import java.io.Serializable;
import java.util.Objects;

public class TrainigSessionModel implements Serializable {
    int id;
    String user_id;
    String event_id;
    String session_id;
    String session_title;
    String session_description;
    String session_image;
    String created_at;
    String updated_at;

    public TrainigSessionModel(String session_id) {
        this.session_id = session_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getEvent_id() {
        return event_id;
    }

    public void setEvent_id(String event_id) {
        this.event_id = event_id;
    }

    public String getSession_id() {
        return session_id;
    }

    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }

    public String getSession_title() {
        return session_title;
    }

    public void setSession_title(String session_title) {
        this.session_title = session_title;
    }

    public String getSession_description() {
        return session_description;
    }

    public void setSession_description(String session_description) {
        this.session_description = session_description;
    }

    public String getSession_image() {
        return session_image;
    }

    public void setSession_image(String session_image) {
        this.session_image = session_image;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TrainigSessionModel)) return false;
        TrainigSessionModel that = (TrainigSessionModel) o;
        return getSession_id().equals(that.getSession_id());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSession_id());
    }
}
