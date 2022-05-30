package com.sk.ultimateplayerhq.models;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import java.io.Serializable;

public class UserModel implements Serializable {

    @SerializedName("username")
    private String username;

    @SerializedName("first_name")
    private String first_name;


    @SerializedName("level_name")
    private String level_name;

    @SerializedName("profile_img")
    private String profile_img;

    @SerializedName("last_name")
    private String last_name;

    @SerializedName("wp_generate_token")
    private String wp_generate_token;

    @SerializedName("id")
    private int id;

    public static UserModel fromJson(JSONObject object) {
        return new Gson().fromJson(object.toString(), UserModel.class);
    }


    public static UserModel fromString(String user) {
        return new Gson().fromJson(user, UserModel.class);
    }

    public static String toString(UserModel model) {
        return new Gson().toJson(model);
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getWp_generate_token() {
        return wp_generate_token;
    }

    public void setWp_generate_token(String wp_generate_token) {
        this.wp_generate_token = wp_generate_token;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getLevel_name() {
        return level_name;
    }

    public void setLevel_name(String level_name) {
        this.level_name = level_name;
    }

    public String getProfile_img() {
        return profile_img;
    }

    public void setProfile_img(String profile_img) {
        this.profile_img = profile_img;
    }
}
