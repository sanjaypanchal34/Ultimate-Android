package com.sk.ultimateplayerhq.models;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.Serializable;

public class CoachModel implements Serializable{

    private  int id;
    private String name;
    private String email;


    public static CoachModel fromJson(JSONObject object) {
        return new Gson().fromJson(object.toString(), CoachModel.class);
    }


    public static CoachModel fromString(String user) {
        return new Gson().fromJson(user, CoachModel.class);
    }

    public static String toString(CoachModel model) {
        return new Gson().toJson(model);
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
